package com.example.lightweaver.mobile.persistence.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.lightweaver.mobile.domain.device.ConnectionType
import com.example.lightweaver.mobile.domain.device.DeviceConfiguration
import com.example.lightweaver.mobile.domain.device.DeviceType
import com.example.lightweaver.mobile.domain.device.DeviceView
import com.example.lightweaver.mobile.domain.device.configuration.ConnectionConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.DeviceTypeConfiguration
import com.example.lightweaver.mobile.persistence.dao.DeviceAndTypeConfigurationDao
import com.example.lightweaver.mobile.persistence.dao.DeviceConfigurationDao
import com.example.lightweaver.mobile.persistence.entities.DeviceAndTypeConfigurationEntity
import com.example.lightweaver.mobile.persistence.entities.DeviceConfigurationEntity
import com.example.lightweaver.mobile.persistence.entities.DeviceTypeConfigurationEntity
import java.net.URI
import java.net.URL

class DeviceConfigurationRepository(
    private val deviceConfigurationDao: DeviceConfigurationDao,
    private val deviceAndTypeConfigurationDao: DeviceAndTypeConfigurationDao) {

    suspend fun insert(device: DeviceConfiguration) {
        val typeConfig = when(device.configuration) {
            is DeviceTypeConfiguration.BasicDeviceConfiguration -> DeviceTypeConfigurationEntity.BasicDeviceConfiguration(device.uid)
            is DeviceTypeConfiguration.LightStripDeviceConfiguration -> DeviceTypeConfigurationEntity.LightStripDeviceConfiguration(device.uid)
            is DeviceTypeConfiguration.TriPanelDeviceConfiguration -> DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration(device.uid)
        }

        val deviceType = DeviceType.fromConfiguration(device.configuration)

        val deviceConfig = when(device.connection) {
            is ConnectionConfiguration.HttpConfiguration -> {
                val httpConfig = DeviceConfigurationEntity.HttpDeviceConfiguration.HttpInfo(
                    device.connection.url.protocol,
                    device.connection.url.host,
                    device.connection.url.port,
                    device.connection.networkName,
                    device.connection.discoverable
                )
                DeviceConfigurationEntity.HttpDeviceConfiguration(
                    device.uid,
                    device.name,
                    device.description,
                    deviceType,
                    httpConfig
                )
            }
        }

        deviceAndTypeConfigurationDao.insertDeviceAndTypeConfiguration(DeviceAndTypeConfigurationEntity(deviceConfig, typeConfig))
    }

    suspend fun get(uid: String): DeviceConfiguration? {
        val deviceConfiguration = deviceAndTypeConfigurationDao.getDevice(uid)
        return deviceConfiguration?.let {
            val typeConfiguration = when(it.typeConfiguration) {
                is DeviceTypeConfigurationEntity.BasicDeviceConfiguration ->
                    DeviceTypeConfiguration.BasicDeviceConfiguration()
                is DeviceTypeConfigurationEntity.LightStripDeviceConfiguration ->
                    DeviceTypeConfiguration.LightStripDeviceConfiguration()
                is DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration ->
                    DeviceTypeConfiguration.LightStripDeviceConfiguration()
            }

            val connectionConfiguration = when (it.deviceConfiguration) {
                is DeviceConfigurationEntity.HttpDeviceConfiguration -> {
                    val url = URL(Uri.Builder()
                        .scheme(it.deviceConfiguration.connectionInfo.protocol)
                        .encodedAuthority("${it.deviceConfiguration.connectionInfo.host}:${it.deviceConfiguration.connectionInfo.port}")
                        .build().toString())
                    ConnectionConfiguration.HttpConfiguration(url, it.deviceConfiguration.connectionInfo.localNetwork, it.deviceConfiguration.connectionInfo.discoverable)
                }
            }

            DeviceConfiguration(it.deviceConfiguration.uid, it.deviceConfiguration.name, it.deviceConfiguration.description, connectionConfiguration, typeConfiguration)
        }
    }

    fun getAll(): LiveData<List<DeviceView>> {
        return Transformations.map(deviceConfigurationDao.getAllDevices()) { list ->
            list.map { config ->
                when (config) {
                    is DeviceConfigurationEntity.HttpDeviceConfiguration -> DeviceView(
                            config.uid,
                            config.name,
                            config.description,
                            false,
                            ConnectionType.HTTP,
                            config.deviceType
                        )
                }
            }
        }
    }

    suspend fun count(): Int {
        return deviceConfigurationDao.count()
    }
}