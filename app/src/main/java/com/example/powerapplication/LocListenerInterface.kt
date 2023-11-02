package com.example.powerapplication

import android.location.Location

interface LocListenerInterface {
    fun onLocationChange(location: Location)
}