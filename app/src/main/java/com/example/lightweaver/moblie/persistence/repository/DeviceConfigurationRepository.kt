package com.example.lightweaver.moblie.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration
import com.example.lightweaver.moblie.domain.device.DeviceSummary
import com.example.lightweaver.moblie.domain.device.connection.HttpConnectionConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightBasicConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration
import com.example.lightweaver.moblie.persistence.dao.DeviceAndTypeConfigurationDao
import com.example.lightweaver.moblie.persistence.dao.DeviceConfigurationDao
import com.example.lightweaver.moblie.persistence.entities.DeviceAndTypeConfiguration
import com.example.lightweaver.moblie.persistence.entities.DeviceType

class DeviceConfigurationRepository(
    private val deviceConfigurationDao: DeviceConfigurationDao,
    private val deviceAndTypeConfigurationDao: DeviceAndTypeConfigurationDao) {

    suspend fun insert(device: DeviceConfiguration) {
        val typeConfig = when(device.typeConfiguration) {
            is LightBasicConfiguration -> com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.BasicDeviceConfiguration(device.uid)
            is LightStripConfiguration -> com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.LightStripDeviceConfiguration(device.uid)
            is LightTriPanelConfiguration -> com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.TriPanelDeviceConfiguration(device.uid)
            else -> throw RuntimeException("Unknown TypeConfiguration: ${device.typeConfiguration::class.simpleName}")
        }

        val deviceType = when(device.typeConfiguration) {
            is LightBasicConfiguration -> DeviceType.BASIC
            is LightStripConfiguration -> DeviceType.LIGHTSTRIP
            is LightTriPanelConfiguration -> DeviceType.TRIPANEL
            else -> throw RuntimeException("Unknown TypeConfiguration: ${device.typeConfiguration::class.simpleName}")
        }

        val deviceConfig = when(device.connectionConfiguration) {
            is HttpConnectionConfiguration -> {
                val httpConfig = com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration.HttpDeviceConfiguration.HttpInfo(
                    device.connectionConfiguration.address,
                    device.connectionConfiguration.port,
                    device.connectionConfiguration.local,
                    device.connectionConfiguration.discoverable
                )
                com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration.HttpDeviceConfiguration(
                    device.uid,
                    device.name,
                    device.description,
                    deviceType,
                    httpConfig
                )
            }
            else -> throw RuntimeException("Unknown ConnectionConfiguration: ${device.connectionConfiguration::class.simpleName}")
        }

        deviceAndTypeConfigurationDao.insertDeviceAndTypeConfiguration(DeviceAndTypeConfiguration(deviceConfig, typeConfig))
    }

    suspend fun get(uid: String): DeviceConfiguration? {
        val deviceConfiguration = deviceAndTypeConfigurationDao.getDevice(uid)
        return deviceConfiguration?.let {
            val typeConfiguration = when(it.typeConfiguration) {
                is com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.BasicDeviceConfiguration ->
                    LightBasicConfiguration()
                is com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.LightStripDeviceConfiguration ->
                    LightStripConfiguration()
                is com.example.lightweaver.moblie.persistence.entities.DeviceTypeConfiguration.TriPanelDeviceConfiguration ->
                    LightTriPanelConfiguration()
                else -> throw RuntimeException("Unknown DeviceTypeConfiguration: ${it.typeConfiguration::class.simpleName}")
            }

            val connectionConfiguration = when (it.deviceConfiguration) {
                is com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration.HttpDeviceConfiguration ->
                    HttpConnectionConfiguration(it.deviceConfiguration.connectionInfo.address, it.deviceConfiguration.connectionInfo.port, it.deviceConfiguration.connectionInfo.local, it.deviceConfiguration.connectionInfo.discoverable)
                else -> throw RuntimeException("Unknown DeviceConfiguration: ${it.deviceConfiguration::class.simpleName}")
            }

            DeviceConfiguration(it.deviceConfiguration.uid, it.deviceConfiguration.name, it.deviceConfiguration.description, connectionConfiguration, typeConfiguration)
        }
    }

    fun getAll(): LiveData<List<DeviceSummary>> {
        return Transformations.map(deviceConfigurationDao.getAllDevices()) { list ->
            list.map { config ->
                val type = when (config.deviceType) {
                    DeviceType.BASIC -> com.example.lightweaver.moblie.domain.device.DeviceType.BASIC
                    DeviceType.LIGHTSTRIP -> com.example.lightweaver.moblie.domain.device.DeviceType.STRIP
                    DeviceType.TRIPANEL -> com.example.lightweaver.moblie.domain.device.DeviceType.TRIPANEL
                }
                when (config) {
                    is com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration.HttpDeviceConfiguration ->
                        DeviceSummary(
                            config.uid,
                            config.name,
                            config.description,
                            HttpConnectionConfiguration(
                                config.connectionInfo.address,
                                config.connectionInfo.port,
                                config.connectionInfo.local,
                                config.connectionInfo.discoverable
                            ),
                            type
                        )
                }
            }
        }
    }

    suspend fun count(): Int {
        return deviceConfigurationDao.count()
    }
}