package com.example.lightweaver.mobile.domain.device

import com.example.lightweaver.mobile.R
import com.example.lightweaver.mobile.domain.device.configuration.DeviceTypeConfiguration

enum class DeviceType(val drawable: Int) {
    BASIC(R.drawable.ic_device_led),
    LIGHT_STRIP(R.drawable.ic_device_light_strip),
    TRI_PANEL(R.drawable.ic_device_tri_panel),
    UNKNOWN(R.drawable.ic_device_unknown);

    companion object {
        fun fromConfiguration(config: DeviceTypeConfiguration): DeviceType {
            return when (config) {
                is DeviceTypeConfiguration.BasicDeviceConfiguration -> BASIC
                is DeviceTypeConfiguration.LightStripDeviceConfiguration -> LIGHT_STRIP
                is DeviceTypeConfiguration.TriPanelDeviceConfiguration -> TRI_PANEL
                else -> UNKNOWN
            }
        }
    }
}