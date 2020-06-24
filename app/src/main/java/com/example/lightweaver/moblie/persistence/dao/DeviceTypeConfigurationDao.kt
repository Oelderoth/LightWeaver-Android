package com.example.lightweaver.moblie.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration

@Dao
abstract class DeviceTypeConfigurationDao {
    @Query("SELECT * FROM basic_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getBasicDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.BasicDeviceConfiguration

    @Update
    abstract suspend fun updateBasicDeviceConfiguration(config: DeviceTypeConfiguration.BasicDeviceConfiguration)


    @Query("SELECT * FROM light_strip_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getLightStripDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.LightStripDeviceConfiguration

    @Update
    abstract suspend fun updateLightStripDeviceConfiguration(config: DeviceTypeConfiguration.LightStripDeviceConfiguration)


    @Query("SELECT * FROM tripanel_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getTriPanelDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.TriPanelDeviceConfiguration

    @Update
    abstract suspend fun updateTriPanelDeviceConfiguration(config: DeviceTypeConfiguration.TriPanelDeviceConfiguration)
}