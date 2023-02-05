package com.example.meltingpot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocViewModel : ViewModel() {
    private var lats = MutableLiveData<Double>()
    private var longs = MutableLiveData<Double>()
    fun setLoc(lat: Double,long: Double) {
        lats.value = lat
        longs.value = long
        longs.postValue(longs.value)
        lats.postValue(lats.value)
    }
    fun getLatVal(): MutableLiveData<Double> {
        return lats
    }
    fun getLongVal(): MutableLiveData<Double> {
        return longs
    }
    fun getLat() {
        lats.postValue(lats.value)
    }
    fun getLong() {
        longs.postValue(longs.value)
    }
}