package com.example.lightweaver.mobile.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class DeviceTypeConfigurationEntity(
    val deviceUid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Entity(tableName = "basic_device_configuration")
    class BasicDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfigurationEntity(deviceUid)

    @Entity(tableName = "light_strip_device_configuration")
    class LightStripDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfigurationEntity(deviceUid)

    @Entity(tableName = "tripanel_device_configuration")
    class TriPanelDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfigurationEntity(deviceUid)
}