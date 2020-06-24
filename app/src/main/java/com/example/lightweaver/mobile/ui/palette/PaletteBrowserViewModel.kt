package com.example.lightweaver.mobile.ui.palette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaletteBrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Palette Browser Fragment"
    }
    val text: LiveData<String> = _text
}