package com.example.lightweaver.moblie.ui.device

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import com.example.lightweaver.moblie.R
import com.example.lightweaver.moblie.databinding.FragmentCreateDeviceBinding
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration
import com.example.lightweaver.moblie.domain.device.connection.HttpConnectionConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightBasicConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration
import kotlinx.android.synthetic.main.fragment_create_device.view.*
import kotlinx.coroutines.launch
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
                "HTTP" -> HttpConnectionConfiguration(viewModel.ipAddress.value!!, viewModel.port.value!!.toInt(), viewModel.localDevice.value!!, viewModel.discoverableDevice.value!!)
                else -> throw RuntimeException("Unknown Connection Config")
            }
            val typeConfig = when(viewModel.deviceType.value) {
                "Basic Light" -> LightBasicConfiguration()
                "Light Strip" -> LightStripConfiguration()
                "TriPanel" -> LightTriPanelConfiguration()
                else -> throw RuntimeException("Unknown Type Config")
            }

            // TODO: Attempt to connect to device and get UID
            val randomUID = Random.nextBytes(4).joinToString("") { "%02x".format(it) }
            val deviceConfiguration = DeviceConfiguration(randomUID, viewModel.deviceName.value!!, null, connectionConfig, typeConfig)

            viewModel.viewModelScope.launch {
                Log.i("LW", "Inserting device ${randomUID} into DB")
                viewModel.insertDevice(deviceConfiguration)
                Log.i("LW", "Insert Complete! Navigating Back")
                findNavController(requireView()).popBackStack(R.id.nav_devices, false)
            }
        }

        return root
    }
}