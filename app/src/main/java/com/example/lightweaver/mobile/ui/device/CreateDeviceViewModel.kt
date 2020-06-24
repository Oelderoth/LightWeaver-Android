package com.example.lightweaver.mobile.ui.device

import android.app.Application
import androidx.lifecycle.*
import com.example.lightweaver.mobile.R
import com.example.lightweaver.mobile.domain.device.DeviceConfiguration
import com.example.lightweaver.mobile.persistence.LightWeaverDatabase
import com.example.lightweaver.mobile.persistence.repository.DeviceConfigurationRepository

class CreateDeviceViewModel(application: Application) : AndroidViewModel(application) {
    private val ipAddressRegex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
    private val mutableDeviceNameError = MutableLiveData<String>()
    private val mutableDeviceTypeError = MutableLiveData<String>()
    private val mutableConnectionTypeError = MutableLiveData<String>()
    private val mutableIpAddressError = MutableLiveData<String>()
    private val mutablePortError = MutableLiveData<String>()

    val deviceName = MutableLiveData<String>()
    val deviceNameError: LiveData<String> = mutableDeviceNameError

    val deviceTypeList = listOf("Basic Light", "Light Strip", "TriPanel")
    val deviceType = MutableLiveData<String>()
    val deviceTypeError: LiveData<String> = mutableDeviceTypeError
    val deviceTypeIcon = Transformations.map(deviceType) { type -> when(type) {
        "Basic Light" -> R.drawable.ic_device_led
        "Light Strip" -> R.drawable.ic_device_light_strip
        "TriPanel" -> R.drawable.ic_device_tri_panel
        else -> R.drawable.ic_device_unknown
    } }

    val connectionTypeList = listOf("HTTP")
    val connectionType = MutableLiveData<String>()
    val connectionTypeError: LiveData<String> = mutableConnectionTypeError
    val connectionTypeIcon: LiveData<Int> = MutableLiveData<Int>().apply { value = R.drawable.ic_wifi_black_24}

    val ipAddress = MutableLiveData<String>()
    val ipAddressError: LiveData<String> = mutableIpAddressError
    val port = MutableLiveData<String>()
    val portError: LiveData<String> = mutablePortError

    val discoverableDevice = MutableLiveData<Boolean>()
    val localDevice = MutableLiveData<Boolean>()

    val advancedSettingsVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val advancedSettingsIcon: LiveData<Int> = Transformations.map(advancedSettingsVisible) {
            visible -> if (visible) R.drawable.ic_arrow_up_24 else R.drawable.ic_arrow_down_24
    }

    val isValid: LiveData<Boolean>
    private val repository: DeviceConfigurationRepository =
        LightWeaverDatabase.getInstance(application).deviceConfigurationRepository()

    init {

        deviceType.value = deviceTypeList[1]
        connectionType.value = connectionTypeList[0]
        port.value = "80"
        discoverableDevice.value = true
        localDevice.value = true

        deviceName.observeForever { name ->
            mutableDeviceNameError.value = when {
                name.isNullOrBlank() -> "Required Field"
                else -> null
            }
        }
        deviceType.observeForever { type -> mutableDeviceTypeError.value = if (!deviceTypeList.contains(type)) "Invalid Value" else null }
        connectionTypeError.observeForever { type -> mutableConnectionTypeError.value = if (!connectionTypeList.contains(type)) "Invalid Value" else null }
        ipAddress.observeForever { ipAddress -> mutableIpAddressError.value = if (!ipAddressRegex.matches(ipAddress)) "Invalid Value" else null }
        port.observeForever { port ->
            val num = port.toIntOrNull()
            mutablePortError.value = if (num == null || num < 0 || num > 65535) "Invalid Value" else null
        }


        val isValidMediator = MediatorLiveData<Boolean>().apply {
            val observer = Observer<String> { value = isValid() }
            value = isValid()

            addSource(deviceName, observer)
            addSource(deviceType, observer)
            addSource(connectionType, observer)
            addSource(ipAddress, observer)
            addSource(port, observer)
        }
        isValid = isValidMediator
    }

    private fun isValid(): Boolean {
        val deviceName = deviceName.value
        val deviceType = deviceType.value
        val connectionType = connectionType.value
        val ipAddress = ipAddress.value
        val port = port.value?.toIntOrNull()

        if (deviceName.isNullOrEmpty() || deviceName?.length > 64) return false
        if (!deviceTypeList.contains(deviceType)) return false
        if (!connectionTypeList.contains(connectionType)) return false
        if (ipAddress == null || !ipAddressRegex.matches(ipAddress)) return false
        if (port == null || port < 0 || port > 65535) return false
        return true
    }

    suspend fun insertDevice(device: DeviceConfiguration) {
        repository.insert(device)
    }

}