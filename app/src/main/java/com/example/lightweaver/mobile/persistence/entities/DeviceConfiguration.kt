package com.example.lightweaver.mobile.persistence.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class DeviceConfiguration(@PrimaryKey val uid: String,
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
    ) : DeviceConfiguration(uid, name, description, deviceType) {

        class HttpInfo(val address: String, val port: Int, val local: Boolean, val discoverable: Boolean)
    }
}

