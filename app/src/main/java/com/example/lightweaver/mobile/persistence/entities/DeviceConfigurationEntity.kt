package com.example.lightweaver.mobile.persistence.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lightweaver.mobile.domain.device.DeviceType

sealed class DeviceConfigurationEntity(@PrimaryKey val uid: String,
                                  val name: String,
                                  val description: String?,
                                  val deviceType: DeviceType){

    @Entity(tableName = "http_device_configuration")
    class HttpDeviceConfiguration(
        uid: String,
        name: String,
        description: String?,
        deviceType: DeviceType,
        @Embedded
        val connectionInfo: HttpInfo
    ) : DeviceConfigurationEntity(uid, name, description, deviceType) {

        class HttpInfo(val protocol: String, val host: String, val port: Int, val localNetwork: String?, val discoverable: Boolean)
    }
}

