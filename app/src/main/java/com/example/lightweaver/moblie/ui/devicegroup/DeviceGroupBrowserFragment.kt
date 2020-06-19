package com.example.lightweaver.moblie.ui.devicegroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lightweaver.moblie.R

class DeviceGroupBrowserFragment : Fragment() {

    private lateinit var deviceGroupBrowserViewModel: DeviceGroupBrowserViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        deviceGroupBrowserViewModel =
                ViewModelProviders.of(this).get(DeviceGroupBrowserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_device_group_browser, container, false)
        val textView: TextView = root.findViewById(R.id.text_device_group_browser)
        deviceGroupBrowserViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}