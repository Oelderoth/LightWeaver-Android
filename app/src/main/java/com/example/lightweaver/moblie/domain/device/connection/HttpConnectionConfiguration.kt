package com.example.lightweaver.moblie.domain.device.connection

import com.example.lightweaver.moblie.domain.device.DeviceConnectionConfiguration

data class HttpConnectionConfiguration(val address: String, val port: Int, val local: Boolean, val discoverable: Boolean): DeviceConnectionConfiguration()