package com.example.powerapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.ActivityMainBinding
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val binding by viewBinding(ActivityMainBinding::bind)
    val REQUEST_OVERLAY_PERMISSION = 1
    var fusedClient: FusedLocationProviderClient? = null

    private var mRequest: LocationRequest? = null
    private var mCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("tagPower","MainActivity")
        init()
    }

    private fun init(){
        chekPermissionGPS()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        //grand()

        if(permissions.entries.toList().all { it.value }){
            grand()
        }else{
            chekPermissionGPS()
        }

    }

    private fun grand(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            /*fusedClient = LocationServices.getFusedLocationProviderClient(this)
            fusedClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val loc = location.provider + " ${formatLocation(location)}"
                    binding.tvLocationGPS.text = "${binding.tvLocationGPS.text} $loc \n"
                }
            }*/
            callbackGPS()
            fusedClient = LocationServices.getFusedLocationProviderClient(this)
            startLocationUpdates()

        }else {
            chekPermissionGPS()
        }
    }

    private fun chekPermissionGPS(){
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
        ))
    }

    private fun callbackGPS(){
        mCallback = object : LocationCallback() {
            //This callback is where we get "streaming" location updates. We can check things like accuracy to determine whether
            //this latest update should replace our previous estimate.
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (loc in locationResult.locations) {
                    val locs = "1)" + "${formatLocation(loc)}"
                    binding.tvLocationGPS.text = "${binding.tvLocationGPS.text} $locs \n"
                }

                val loc = "2)" + formatLocation(locationResult.lastLocation)
                binding.tvLocationGPS.text = "${binding.tvLocationGPS.text} $loc \n"
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
            }
        }

        mRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        } /*LocationRequest().apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }*/
    }

    private fun createLocationRequest() {
        val mLocationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun onPause() {
        super.onPause()
        fusedClient?.removeLocationUpdates(mCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedClient?.requestLocationUpdates(mRequest, mCallback, null)
    }

    @SuppressLint("DefaultLocale")
    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else "${location.latitude}, ${location.longitude}"
    }
}