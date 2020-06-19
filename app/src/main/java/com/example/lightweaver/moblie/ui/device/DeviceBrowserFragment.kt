package com.example.lightweaver.moblie.ui.device

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lightweaver.moblie.R
import com.google.android.material.snackbar.Snackbar

class DeviceBrowserFragment : Fragment() {

    private lateinit var deviceBrowserViewModel: DeviceBrowserViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        deviceBrowserViewModel =
                ViewModelProviders.of(this).get(DeviceBrowserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_device_browser, container, false)
        val textView: TextView = root.findViewById(R.id.text_device_browser)
        deviceBrowserViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
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