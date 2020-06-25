package com.example.lightweaver.mobile.domain.device

import com.example.lightweaver.mobile.domain.device.configuration.ConnectionConfiguration
import com.example.lightweaver.mobile.domain.device.configuration.DeviceTypeConfiguration

/**
 * Provides a full overview of all configured settings for a device
 */
class DeviceConfiguration(
    val uid: String,
    val name: String,
    val description: String?,
    val connection: ConnectionConfiguration,
    val configuration: DeviceTypeConfiguration
)