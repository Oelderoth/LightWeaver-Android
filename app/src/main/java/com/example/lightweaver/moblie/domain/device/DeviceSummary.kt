package com.example.lightweaver.moblie.domain.device

open class DeviceSummary(val uid: String, val name: String, val description: String?, val connectionConfiguration: DeviceConnectionConfiguration, val deviceType: DeviceType)

