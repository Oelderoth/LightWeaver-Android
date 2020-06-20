package com.example.lightweaver.moblie.ui.device

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweaver.moblie.R
import com.google.android.material.snackbar.Snackbar
import com.oelderoth.lightweaver.core.devices.SupportedFeature
import com.oelderoth.lightweaver.http.devices.HttpDeviceDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DeviceBrowserFragment : Fragment() {

    private lateinit var viewModel: DeviceBrowserViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProviders.of(this).get(DeviceBrowserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_device_browser, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.device_browser_view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = DeviceBrowserRecyclerViewAdapter(requireContext(), viewModel.deviceDescriptorList.value ?: listOf())

        viewModel.deviceDescriptorList.observe(viewLifecycleOwner, Observer {
            recyclerView.swapAdapter(DeviceBrowserRecyclerViewAdapter(requireContext(), viewModel.deviceDescriptorList.value ?: listOf()), false)
        })

        viewModel.addDeviceDescriptor(HttpDeviceDescriptor("0x00101010A", "Not a real device", "0.0.1", listOf(SupportedFeature.ADDRESSABLE, SupportedFeature.ANIMATION, SupportedFeature.BRIGHTNESS, SupportedFeature.COLOR), "0.1", "http://192.168.0.145:80/"))
        viewModel.addDeviceDescriptor(HttpDeviceDescriptor("0x00101010B", "Dev 2", "0.0.1", listOf(SupportedFeature.ADDRESSABLE, SupportedFeature.ANIMATION, SupportedFeature.BRIGHTNESS, SupportedFeature.COLOR), "0.1", "http://192.168.0.145:80/"))

        viewModel.viewModelScope.launch {
            delay(2000)
            viewModel.addDeviceDescriptor(HttpDeviceDescriptor("0x00101010C", "Slow Device", "0.0.1", listOf(SupportedFeature.ADDRESSABLE, SupportedFeature.ANIMATION, SupportedFeature.BRIGHTNESS, SupportedFeature.COLOR), "0.1", "http://192.168.0.145:80/"))
            delay(10000)
            viewModel.addDeviceDescriptor(HttpDeviceDescriptor("0x00101010D", "Really Slow Device", "0.0.1", listOf(SupportedFeature.ADDRESSABLE, SupportedFeature.ANIMATION, SupportedFeature.BRIGHTNESS, SupportedFeature.COLOR), "0.1", "http://192.168.0.145:80/"))
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.device_browser_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_misc -> Snackbar.make(requireView(), "Do Miscellaneous?", Snackbar.LENGTH_LONG)
                .setAction("Action") { _ ->  Log.i("TAG", "Misc Action Performed")}.show()
            else -> return false
        }
        return true
    }
}
