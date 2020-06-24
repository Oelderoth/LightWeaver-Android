package com.example.lightweaver.mobile.ui.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lightweaver.mobile.R

class SceneBrowserFragment : Fragment() {

    private lateinit var sceneBrowserViewModel: SceneBrowserViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sceneBrowserViewModel =
                ViewModelProviders.of(this).get(SceneBrowserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_scene_browser, container, false)
        val textView: TextView = root.findViewById(R.id.text_scene_browser)
        sceneBrowserViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}