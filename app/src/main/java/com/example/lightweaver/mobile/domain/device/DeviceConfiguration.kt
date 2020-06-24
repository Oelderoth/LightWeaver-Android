package com.example.lightweaver.mobile.domain.device

class DeviceConfiguration(
    val uid: String,
    val name: String,
    val description: String?,
    val connectionConfiguration: DeviceConnectionConfiguration,
    val typeConfiguration: DeviceTypeConfiguration)