package com.example.lightweaver.mobile.ui.palette

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.lightweaver.mobile.R
import com.example.lightweaver.mobile.ui.settings.SettingsFragment

class PaletteBrowserFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: PaletteBrowserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(PaletteBrowserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_palette_browser, container, false)
        val textView: TextView = root.findViewById(R.id.text_palette_browser)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}