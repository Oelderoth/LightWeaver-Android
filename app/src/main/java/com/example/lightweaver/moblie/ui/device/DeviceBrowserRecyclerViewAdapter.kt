package com.example.lightweaver.moblie.ui.device

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweaver.moblie.R
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration

class DeviceBrowserRecyclerViewAdapter(context: Context, private val deviceConfigurations: List<DeviceConfiguration>):
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
        holder.deviceDescription.text = deviceConfiguration.description
        holder.itemView.setOnClickListener { Log.i("LW","Tapped on ${deviceConfiguration.name}") }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.device_name_text)
        val deviceDescription: TextView = itemView.findViewById(R.id.device_description_text)
        val deviceTypeIcon: ImageView = itemView.findViewById(R.id.device_type_icon)
    }
}