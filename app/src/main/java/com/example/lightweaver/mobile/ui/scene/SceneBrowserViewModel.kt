package com.example.lightweaver.mobile.ui.scene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SceneBrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Scene Browser Fragment"
    }
    val text: LiveData<String> = _text
}