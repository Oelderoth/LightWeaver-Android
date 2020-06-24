package com.example.lightweaver.mobile.domain.device.connection

import com.example.lightweaver.mobile.domain.device.DeviceConnectionConfiguration

data class HttpConnectionConfiguration(val address: String, val port: Int, val local: Boolean, val discoverable: Boolean): DeviceConnectionConfiguration()