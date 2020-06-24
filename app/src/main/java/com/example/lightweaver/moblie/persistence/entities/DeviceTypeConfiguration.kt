package com.example.lightweaver.moblie.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class DeviceTypeConfiguration(
    val deviceUid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Entity(tableName = "basic_device_configuration")
    class BasicDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfiguration(deviceUid)

    @Entity(tableName = "light_strip_device_configuration")
    class LightStripDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfiguration(deviceUid)

    @Entity(tableName = "tripanel_device_configuration")
    class TriPanelDeviceConfiguration(
        deviceUid: String
    ): DeviceTypeConfiguration(deviceUid)
}