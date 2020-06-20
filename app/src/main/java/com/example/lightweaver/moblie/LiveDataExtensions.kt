package com.example.lightweaver.moblie

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}