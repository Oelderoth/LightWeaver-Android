package com.example.lightweaver.moblie.ui.device

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweaver.moblie.R
import com.example.lightweaver.moblie.domain.device.type.LightBasicConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration

class DeviceBrowserRecyclerViewAdapter(val context: Context, private val deviceConfigurations: List<DeviceBrowserViewModel.DeviceInfo>):
    RecyclerView.Adapter<DeviceBrowserRecyclerViewAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.device_browser_list_row, parent, false);
        return ViewHolder(view)
    }

    // Convert the descriptor UID to a Long so that they can be uniquely identified regardless of position
    override fun getItemId(position: Int): Long = deviceConfigurations[position].uid.hashCode().toLong()

    override fun getItemCount(): Int = deviceConfigurations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceConfiguration = deviceConfigurations[position]
        holder.deviceName.text = deviceConfiguration.name
        holder.deviceDescription.text = deviceConfiguration.description ?: "No Description"
        holder.deviceTypeIcon.setImageDrawable(ContextCompat.getDrawable(context, deviceConfiguration.type.drawable))
        holder.deviceTypeIcon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, deviceConfiguration.connection.iconColor), PorterDuff.Mode.SRC_IN)
        holder.deviceConnectionErrorFlag.visibility = if (deviceConfiguration.connection.errorFlagVisible) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener { Log.i("LW","Tapped on ${deviceConfiguration.name}") }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.device_name_text)
        val deviceDescription: TextView = itemView.findViewById(R.id.device_description_text)
        val deviceTypeIcon: ImageView = itemView.findViewById(R.id.device_type_icon)
        val deviceConnectionErrorFlag: ImageView = itemView.findViewById(R.id.device_connection_error)
    }
}