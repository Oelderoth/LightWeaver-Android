package com.example.lightweaver.mobile.domain.device

import com.example.lightweaver.mobile.R

enum class DeviceType(val drawable: Int) {
    BASIC(R.drawable.ic_device_led),
    STRIP(R.drawable.ic_device_light_strip),
    TRIPANEL(R.drawable.ic_device_tri_panel),
    UNKNOWN(R.drawable.ic_device_unknown)
}