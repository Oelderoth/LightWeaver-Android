package com.example.lightweaver.moblie.domain.device

class DeviceConfiguration(
    val uid: String,
    val name: String,
    val description: String?,
    val connectionConfiguration: DeviceConnectionConfiguration,
    val typeConfiguration: DeviceTypeConfiguration)