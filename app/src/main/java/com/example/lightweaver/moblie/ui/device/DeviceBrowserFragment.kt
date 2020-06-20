package com.example.lightweaver.moblie.ui.device

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweaver.moblie.R
import com.google.android.material.snackbar.Snackbar


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
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = DeviceBrowserRecyclerViewAdapter(requireContext(), viewModel.deviceConfigurations.value ?: listOf())

        viewModel.deviceConfigurations.observe(viewLifecycleOwner, Observer {
            recyclerView.swapAdapter(DeviceBrowserRecyclerViewAdapter(requireContext(), viewModel.deviceConfigurations.value ?: listOf()), false)
        })

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
