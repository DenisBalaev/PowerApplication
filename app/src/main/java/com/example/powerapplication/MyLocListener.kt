package com.example.powerapplication

import android.location.Location
import android.location.LocationListener

class MyLocListener:LocationListener {

    private var locListenerInterface: LocListenerInterface? = null

    fun setLocListenerInterface(locListenerInterface: LocListenerInterface?) {
        this.locListenerInterface = locListenerInterface
    }

    override fun onLocationChanged(location: Location) {
        locListenerInterface?.onLocationChange(location)
    }
}