package com.example.lightweaver.moblie.ui.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweaver.moblie.R
import com.oelderoth.lightweaver.core.devices.DeviceDescriptor

class DeviceBrowserRecyclerViewAdapter(context: Context, private val devices: List<DeviceDescriptor>):
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
    override fun getItemId(position: Int): Long = devices[position].uid.hashCode().toLong()

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val descriptor = devices[position]
        holder.deviceName.text = descriptor.name
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deviceName: TextView = itemView.findViewById(R.id.device_name_text)
    }

}