package com.example.lightweaver.mobile.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.lightweaver.mobile.persistence.entities.DeviceTypeConfigurationEntity

@Dao
abstract class DeviceTypeConfigurationDao {
    @Query("SELECT * FROM basic_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getBasicDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.BasicDeviceConfiguration

    @Update
    abstract suspend fun updateBasicDeviceConfiguration(config: DeviceTypeConfigurationEntity.BasicDeviceConfiguration)


    @Query("SELECT * FROM light_strip_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getLightStripDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.LightStripDeviceConfiguration

    @Update
    abstract suspend fun updateLightStripDeviceConfiguration(config: DeviceTypeConfigurationEntity.LightStripDeviceConfiguration)


    @Query("SELECT * FROM tripanel_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getTriPanelDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration

    @Update
    abstract suspend fun updateTriPanelDeviceConfiguration(config: DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration)
}