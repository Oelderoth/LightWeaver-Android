package com.example.lightweaver.mobile.domain.device

/**
 * Represents a general overview of a configured device, for scenarios when
 * explicit device configuration isn't required
 */
class DeviceView(
    val uid: String,
    val name: String,
    val description: String?,
    var connected: Boolean,
    val connectionType: ConnectionType,
    val deviceType: DeviceType
)