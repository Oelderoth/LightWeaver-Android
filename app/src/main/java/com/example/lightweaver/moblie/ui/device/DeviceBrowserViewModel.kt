package com.example.lightweaver.moblie.ui.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.lightweaver.moblie.R
import com.example.lightweaver.moblie.domain.device.DeviceType
import com.example.lightweaver.moblie.domain.device.type.LightBasicConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration
import com.example.lightweaver.moblie.persistence.LightWeaverDatabase

class DeviceBrowserViewModel(application: Application) : AndroidViewModel(application) {
    val deviceInfo: LiveData<List<DeviceInfo>>
    init {
        val repository = LightWeaverDatabase.getInstance(application).deviceConfigurationRepository()
        deviceInfo = Transformations.map(repository.getAll()) { configs ->
            configs.map { config ->
                DeviceInfo(config.uid, config.name, config.description, config.deviceType, ConnectionState.UNKNOWN)
            }
        }
    }

    enum class ConnectionState(val iconColor: Int, val errorFlagVisible: Boolean) {
        UNKNOWN(R.color.deviceInactive, false),
        CONNECTED(R.color.deviceActive, false),
        DISCONNECTED(R.color.deviceInactive, true)
    }

    data class DeviceInfo(val uid: String, val name: String, val description: String?, val type: DeviceType, val connection: ConnectionState)
}