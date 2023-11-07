package com.example.powerapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val binding by viewBinding(ActivityMainBinding::bind)
    val REQUEST_OVERLAY_PERMISSION = 1
    private var locationManager: LocationManager? = null

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

            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 25000, 1f, locationListener)
            }
            if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 25000, 1f, locationListener)
            }
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

    private var str = ""

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            str += "1)" + "${formatLocation(location)} \n"
            binding.tvLocationGPS.text = str
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    @SuppressLint("DefaultLocale")
    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else "${location.latitude}, ${location.longitude}"
    }
}