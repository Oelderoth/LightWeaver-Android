package com.example.lightweaver.mobile.ui.device

import android.app.Activity
import android.content.Context
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
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
import com.example.lightweaver.mobile.R
import com.example.lightweaver.mobile.databinding.FragmentCreateDeviceBinding
import com.example.lightweaver.mobile.domain.device.DeviceConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.ConnectionConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.DeviceTypeConfiguration
import kotlinx.android.synthetic.main.fragment_create_device.view.*
import kotlinx.coroutines.launch
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

        root.create_device_button.setOnClickListener {
            val connectionConfig = when(viewModel.connectionType.value) {
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
            val typeConfig = when(viewModel.deviceType.value) {
                "Basic Light" -> DeviceTypeConfiguration.BasicDeviceConfiguration()
                "Light Strip" -> DeviceTypeConfiguration.LightStripDeviceConfiguration()
                "TriPanel" -> DeviceTypeConfiguration.TriPanelDeviceConfiguration()
                else -> throw RuntimeException("Unknown Type Config")
            }

            // TODO: Verify device is connectible, and retrieve the UID
            val randomUID = Random.nextBytes(4).joinToString("") { "%02x".format(it) }
            val deviceConfiguration = DeviceConfiguration(randomUID, viewModel.deviceName.value!!, null, connectionConfig, typeConfig)

            viewModel.viewModelScope.launch {
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
}