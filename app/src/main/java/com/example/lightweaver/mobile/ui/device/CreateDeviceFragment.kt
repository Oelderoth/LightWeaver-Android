package com.example.lightweaver.mobile.ui.device

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.lightweaver.mobile.R
import com.example.lightweaver.mobile.databinding.FragmentCreateDeviceBinding
import com.example.lightweaver.mobile.domain.device.DeviceConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.ConnectionConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.DeviceTypeConfiguration
import com.example.lightweaver.mobile.persistence.LightWeaverDatabase
import com.oelderoth.lightweaver.http.devices.HttpDevice
import kotlinx.android.synthetic.main.fragment_create_device.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL
import kotlin.random.Random


class CreateDeviceFragment : Fragment() {

    companion object {
        fun newInstance() = CreateDeviceFragment()
    }

    private lateinit var viewModel: CreateDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(CreateDeviceViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentCreateDeviceBinding>(inflater, R.layout.fragment_create_device, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val root = binding.root

        root.advanced_settings_text.setOnClickListener {
            viewModel.advancedSettingsVisible.value = !(viewModel.advancedSettingsVisible.value ?: false)
        }

        root.create_device_content_layout.addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
            val height = bottom - top
            val scrollHeight = root.create_device_scroll_layout.height
            root.divider.visibility = if (height >= scrollHeight) View.VISIBLE else View.INVISIBLE
        }

        val hideKeyboardOnFocusListener = View.OnFocusChangeListener { _, focused ->
            if (focused) {
                val imm =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(root.windowToken, 0)
            }
        }

        root.device_type_text.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_list_item, viewModel.deviceTypeList))
        root.device_type_text.onFocusChangeListener = hideKeyboardOnFocusListener

        root.device_connection_text.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_list_item, viewModel.connectionTypeList))
        root.device_connection_text.onFocusChangeListener = hideKeyboardOnFocusListener

        viewModel.deviceNameError.observe(viewLifecycleOwner, Observer { error -> root.device_name_layout.error = error})
        viewModel.deviceTypeError.observe(viewLifecycleOwner, Observer { error -> root.device_type_layout.error = error})
        viewModel.connectionTypeError.observe(viewLifecycleOwner, Observer { error -> root.device_connection_layout.error = error})
        viewModel.ipAddressError.observe(viewLifecycleOwner, Observer { error -> root.device_ipaddress_layout.error = error})
        viewModel.portError.observe(viewLifecycleOwner, Observer { error -> root.device_port_layout.error = error})
        viewModel.isValid.observe(viewLifecycleOwner, Observer { valid -> root.create_device_button.isEnabled = valid })

        val progressDrawable = CircularProgressDrawable(requireContext()).apply {
            // let's use large style just to better see one issue
            setStyle(CircularProgressDrawable.DEFAULT)
            setColorSchemeColors(Color.WHITE)

            callback = object: Drawable.Callback {
                override fun scheduleDrawable(p0: Drawable, p1: Runnable, p2: Long) = Unit
                override fun unscheduleDrawable(p0: Drawable, p1: Runnable) = Unit
                override fun invalidateDrawable(drawable: Drawable) = root.create_device_button.invalidate()
            }

            start()
        }

        root.create_device_button.setOnClickListener {
            root.create_device_button.isClickable = false
            root.create_device_button.icon = progressDrawable
            root.create_device_button.text = "Connecting"
            root.connection_error.visibility = View.GONE

            viewModel.viewModelScope.launch {
                val connectionConfig = buildConnectionConfiguration(viewModel)
                val typeConfig = buildTypeConfiguration(viewModel)

                val deviceUid = withContext(Dispatchers.IO) {
                      tryGetDeviceUid(connectionConfig)
                }

                val existingDevice = withContext(Dispatchers.IO) {
                    deviceUid?.let { LightWeaverDatabase.getInstance(requireContext()).deviceConfigurationRepository().get(deviceUid) }
                }

                if (deviceUid.isNullOrEmpty() || existingDevice != null) {
                    root.create_device_button.isClickable = true
                    root.create_device_button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_white_24)
                    root.create_device_button.text = "Create"
                    root.error_text.text = when {
                        deviceUid.isNullOrEmpty() -> "Unable to connect to device.\nIs your connection information correct?"
                        existingDevice != null -> "This device is already registered with the nickname \"${existingDevice.name}\""
                        else -> "An Error Occurred"
                    }
                    root.connection_error.visibility = View.VISIBLE
                    return@launch
                }

                val deviceConfiguration = DeviceConfiguration(deviceUid, viewModel.deviceName.value!!, null, connectionConfig, typeConfig)

                viewModel.insertDevice(deviceConfiguration)

                findNavController(requireView()).popBackStack(R.id.nav_devices, false)
            }
        }

        return root
    }

    private fun getNetworkSSID(context: Context): String? {
        val manager = context.getSystemService(WifiManager::class.java)
        if (manager.isWifiEnabled) {
                val wifiInfo = manager.connectionInfo
                if (wifiInfo != null) {
                    val state = WifiInfo.getDetailedStateOf(wifiInfo.supplicantState);
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                        return wifiInfo.ssid;
                    }
                }
        }
        return null
    }

    private fun buildConnectionConfiguration(viewModel: CreateDeviceViewModel): ConnectionConfiguration {
        return when(viewModel.connectionType.value) {
            "HTTP" -> {
                val url = URL(Uri.Builder()
                    .scheme("http")
                    .encodedAuthority("${viewModel.ipAddress.value!!}:${viewModel.port.value!!.toInt()}")
                    .build().toString())
                val localNetwork = if (viewModel.localDevice.value!!) getNetworkSSID(requireContext()) else null
                ConnectionConfiguration.HttpConfiguration(url, localNetwork, viewModel.discoverableDevice.value!!)
            }
            else -> throw RuntimeException("Unknown Connection Config")
        }
    }

    private fun buildTypeConfiguration(viewModel: CreateDeviceViewModel): DeviceTypeConfiguration {
        return when(viewModel.deviceType.value) {
            "Basic Light" -> DeviceTypeConfiguration.BasicDeviceConfiguration()
            "Light Strip" -> DeviceTypeConfiguration.LightStripDeviceConfiguration()
            "TriPanel" -> DeviceTypeConfiguration.TriPanelDeviceConfiguration()
            else -> throw RuntimeException("Unknown Type Config")
        }
    }

    private fun tryGetDeviceUid(connectionConfiguration: ConnectionConfiguration): String? {
        val device = when (connectionConfiguration) {
            is ConnectionConfiguration.HttpConfiguration -> HttpDevice(connectionConfiguration.url)
        }

        return try {
            val descriptor = device.GetDeviceDescriptor()
            descriptor.uid
        } catch (e: Exception) {
            Log.w("LW", "Failed to retrieve device descriptor: $e", e)
            null;
        }
    }
}