package com.example.lightweaver.mobile.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweaver.mobile.domain.device.DeviceType
import com.example.lightweaver.mobile.persistence.entities.*

/**
 * To ensure all devices and types are inserted and removed simultaneously, they are handled in tandem
 * via this class rather than via their individual DAOs
 */
@Dao
abstract class DeviceAndTypeConfigurationDao {
    @Query("SELECT * FROM http_device_configuration")
    protected abstract fun getAllHttpDevices(): LiveData<List<DeviceConfigurationEntity.HttpDeviceConfiguration>>

    @Query("SELECT * FROM http_device_configuration WHERE uid == :uid LIMIT 1")
    abstract suspend fun getHttpDevice(uid: String): DeviceConfigurationEntity.HttpDeviceConfiguration?
    @Update
    abstract suspend fun updateHttpDevice(deviceConfiguration: DeviceConfigurationEntity.HttpDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertHttpDevice(deviceConfiguration: DeviceConfigurationEntity.HttpDeviceConfiguration)

    @Query("SELECT * FROM basic_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getBasicDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.BasicDeviceConfiguration
    @Update
    abstract suspend fun updateBasicDeviceConfiguration(config: DeviceTypeConfigurationEntity.BasicDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertBasicDeviceConfiguration(config: DeviceTypeConfigurationEntity.BasicDeviceConfiguration)


    @Query("SELECT * FROM light_strip_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getLightStripDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.LightStripDeviceConfiguration
    @Update
    abstract suspend fun updateLightStripDeviceConfiguration(config: DeviceTypeConfigurationEntity.LightStripDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertLightStripDeviceConfiguration(config: DeviceTypeConfigurationEntity.LightStripDeviceConfiguration)

    @Query("SELECT * FROM tripanel_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getTriPanelDeviceConfiguration(deviceUid: String): DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration
    @Update
    abstract suspend fun updateTriPanelDeviceConfiguration(config: DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertTriPanelDeviceConfiguration(config: DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration)

    @Transaction
    open suspend fun insertDeviceAndTypeConfiguration(deviceAndTypeConfiguration: DeviceAndTypeConfigurationEntity) {
        val device = deviceAndTypeConfiguration.deviceConfiguration
        val type = deviceAndTypeConfiguration.typeConfiguration

        //TODO: Validate type

        when (device) {
            is DeviceConfigurationEntity.HttpDeviceConfiguration -> insertHttpDevice(device)
        }

        when (type) {
            is DeviceTypeConfigurationEntity.BasicDeviceConfiguration -> insertBasicDeviceConfiguration(type)
            is DeviceTypeConfigurationEntity.LightStripDeviceConfiguration -> insertLightStripDeviceConfiguration(type)
            is DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration -> insertTriPanelDeviceConfiguration(type)
        }
    }

    @Transaction
    open suspend fun getDevice(deviceUid: String): DeviceAndTypeConfigurationEntity? {
        val httpDevice = getHttpDevice(deviceUid)
        return httpDevice?.let {device ->
            when(device.deviceType) {
                DeviceType.BASIC -> getBasicDeviceConfiguration(deviceUid)
                DeviceType.LIGHT_STRIP -> getLightStripDeviceConfiguration(deviceUid)
                DeviceType.TRI_PANEL -> getTriPanelDeviceConfiguration(deviceUid)
                else -> throw RuntimeException("Unable to load device of type ${device.deviceType.name}")
            }.let { typeConfiguration ->
                DeviceAndTypeConfigurationEntity(device, typeConfiguration)
            }
        }
    }
}