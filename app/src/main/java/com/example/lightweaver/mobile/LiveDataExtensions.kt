package com.example.lightweaver.mobile

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}