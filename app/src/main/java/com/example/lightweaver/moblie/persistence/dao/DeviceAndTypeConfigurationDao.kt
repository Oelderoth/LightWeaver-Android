package com.example.lightweaver.moblie.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweaver.moblie.persistence.entities.DeviceAndTypeConfiguration
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration
import com.example.lightweaver.moblie.persistence.entities.DeviceType
import com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration

/**
 * To ensure all devices and types are inserted and removed simultaneously, they are handled in tandem
 * via this class rather than via their individual DAOs
 */
@Dao
abstract class DeviceAndTypeConfigurationDao {
    @Query("SELECT * FROM http_device_configuration")
    protected abstract fun getAllHttpDevices(): LiveData<List<DeviceConfiguration.HttpDeviceConfiguration>>

    @Query("SELECT * FROM http_device_configuration WHERE uid == :uid LIMIT 1")
    abstract suspend fun getHttpDevice(uid: String): DeviceConfiguration.HttpDeviceConfiguration?
    @Update
    abstract suspend fun updateHttpDevice(deviceConfiguration: DeviceConfiguration.HttpDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertHttpDevice(deviceConfiguration: DeviceConfiguration.HttpDeviceConfiguration)

    @Query("SELECT * FROM basic_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getBasicDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.BasicDeviceConfiguration
    @Update
    abstract suspend fun updateBasicDeviceConfiguration(config: DeviceTypeConfiguration.BasicDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertBasicDeviceConfiguration(config: DeviceTypeConfiguration.BasicDeviceConfiguration)


    @Query("SELECT * FROM light_strip_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getLightStripDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.LightStripDeviceConfiguration
    @Update
    abstract suspend fun updateLightStripDeviceConfiguration(config: DeviceTypeConfiguration.LightStripDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertLightStripDeviceConfiguration(config: DeviceTypeConfiguration.LightStripDeviceConfiguration)

    @Query("SELECT * FROM tripanel_device_configuration WHERE deviceUid == :deviceUid LIMIT 1")
    abstract suspend fun getTriPanelDeviceConfiguration(deviceUid: String): DeviceTypeConfiguration.TriPanelDeviceConfiguration
    @Update
    abstract suspend fun updateTriPanelDeviceConfiguration(config: DeviceTypeConfiguration.TriPanelDeviceConfiguration)
    @Insert
    protected abstract suspend fun insertTriPanelDeviceConfiguration(config: DeviceTypeConfiguration.TriPanelDeviceConfiguration)

    @Transaction
    open suspend fun insertDeviceAndTypeConfiguration(deviceAndTypeConfiguration: DeviceAndTypeConfiguration) {
        val device = deviceAndTypeConfiguration.deviceConfiguration
        val type = deviceAndTypeConfiguration.typeConfiguration

        //TODO: Validate type

        when (device) {
            is DeviceConfiguration.HttpDeviceConfiguration -> insertHttpDevice(device)
            else -> throw RuntimeException("Unknown Device Configuration: ${device::class.simpleName}")
        }

        when (type) {
            is DeviceTypeConfiguration.BasicDeviceConfiguration -> insertBasicDeviceConfiguration(type)
            is DeviceTypeConfiguration.LightStripDeviceConfiguration -> insertLightStripDeviceConfiguration(type)
            is DeviceTypeConfiguration.TriPanelDeviceConfiguration -> insertTriPanelDeviceConfiguration(type)
            else -> throw RuntimeException("Unknown Type Configuration: ${type::class.simpleName}")
        }
    }

    @Transaction
    open suspend fun getDevice(deviceUid: String): DeviceAndTypeConfiguration? {
        val device = getHttpDevice(deviceUid)
        return device?.let {device ->
            when(device.deviceType) {
                DeviceType.BASIC -> getBasicDeviceConfiguration(deviceUid)
                DeviceType.LIGHTSTRIP -> getLightStripDeviceConfiguration(deviceUid)
                DeviceType.TRIPANEL -> getTriPanelDeviceConfiguration(deviceUid)
            }.let { typeConfiguration ->
                DeviceAndTypeConfiguration(device, typeConfiguration)
            }
        }
    }
}