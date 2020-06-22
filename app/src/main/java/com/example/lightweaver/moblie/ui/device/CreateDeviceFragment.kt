package com.example.lightweaver.moblie.ui.device

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.lightweaver.moblie.R
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration
import com.example.lightweaver.moblie.domain.device.connection.HttpConnectionConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightBasicConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.lang.RuntimeException


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
        val root = inflater.inflate(R.layout.fragment_create_device, container, false)

        val toggleAdvancedSettings = root.findViewById<TextView>(R.id.advanced_settings_text)
        val advancedSettingsGroup = root.findViewById<Group>(R.id.advanced_settings_group)
        toggleAdvancedSettings.setOnClickListener {
            val isVisible = when (advancedSettingsGroup.visibility) {
                View.VISIBLE -> false
                else -> true
            }

            val settingsVisibility = if (isVisible) View.VISIBLE else View.GONE
            val arrowDrawable = if (isVisible) R.drawable.ic_arrow_up_24 else R.drawable.ic_arrow_down_24

            advancedSettingsGroup.visibility = settingsVisibility
            toggleAdvancedSettings.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireContext(), arrowDrawable), null)
        }

        val divider = root.findViewById<View>(R.id.divider)
        val scrollLayout = root.findViewById<NestedScrollView>(R.id.create_device_scroll_layout)
        val contentLayout = root.findViewById<ConstraintLayout>(R.id.create_device_content_layout)
        contentLayout.addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
            val height = bottom - top
            val scrollHeight = scrollLayout.height
            divider.visibility = if (height >= scrollHeight) View.VISIBLE else View.INVISIBLE
        }

        val hideKeyboardOnFocusListener = View.OnFocusChangeListener { _, focused ->
            if (focused) {
                val imm =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(root.windowToken, 0)
            }
        }

        val deviceName = root.findViewById<TextInputEditText>(R.id.device_name_text)
        val deviceType = root.findViewById<AutoCompleteTextView>(R.id.device_type_text)
        val deviceConnection = root.findViewById<AutoCompleteTextView>(R.id.device_connection_text)
        val deviceIpAddress = root.findViewById<TextInputEditText>(R.id.device_ipaddress_text)
        val devicePort = root.findViewById<TextInputEditText>(R.id.device_port_text)
        val deviceDiscoverable = root.findViewById<CheckBox>(R.id.device_discoverable_check)
        val deviceLocal = root.findViewById<CheckBox>(R.id.device_local_check)
        val createButton = root.findViewById<MaterialButton>(R.id.create_device_button)

        val deviceTypes = listOf("Basic Light", "Light Strip", "TriPanel")
        deviceType.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_list_item, deviceTypes))
        deviceType.setText(deviceTypes[0], false)
        deviceType.onFocusChangeListener = hideKeyboardOnFocusListener

        val connectionTypes = listOf("HTTP")
        deviceConnection.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_list_item, connectionTypes))
        deviceConnection.setText(connectionTypes[0], false)
        deviceConnection.onFocusChangeListener = hideKeyboardOnFocusListener

        createButton.setOnClickListener {
            val name = deviceName.text.toString()
            val type = deviceType.text.toString()
            val connection = deviceConnection.text.toString()
            val ipAddress = deviceIpAddress.text.toString()
            val portString = devicePort.text.toString()
            val discoverable = deviceDiscoverable.isChecked
            val local = deviceLocal.isChecked

            //TODO: Validation error messages
            if (name == null || name.isEmpty() || type.isEmpty() || connection.isEmpty() || ipAddress == null || ipAddress.isEmpty() || portString == null || portString.isEmpty()) {
                return@setOnClickListener
            }
            val ipRegex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
            val port = portString.toIntOrNull()
            if (!ipRegex.matches(ipAddress) || port == null) {
                return@setOnClickListener
            }

            val connectionConfig = when(connection) {
                "HTTP" -> HttpConnectionConfiguration(ipAddress.toString(), port, local, discoverable)
                else -> throw RuntimeException("Unknown Connection Config")
            }
            val typeConfig = when(type) {
                "Basic Light" -> LightBasicConfiguration()
                "Light Strip" -> LightStripConfiguration()
                "TriPanel" -> LightTriPanelConfiguration()
                else -> throw RuntimeException("Unknown Type Config")
            }

            // TODO: Attempt to connect to device and get UID
            val deviceConfiguration = DeviceConfiguration("", name, "", connectionConfig, typeConfig)

            // TODO: Insert into DB

            findNavController().navigate(R.id.action_end_create_device)
        }

        return root
    }
}