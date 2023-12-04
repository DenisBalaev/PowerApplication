package com.example.powerapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import timber.log.Timber


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val binding by viewBinding(ActivityMainBinding::bind)
    val REQUEST_OVERLAY_PERMISSION = 1
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*startActivity(Intent(this@MainActivity,FragmentActivity::class.java))
        finish()*/
        /*Log.d("tagPower","MainActivity")*/
        init()
    }

    private fun init(){
        chekPermissionGPS()

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }*/
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
            Log.d("StartMyActivityAtBoot","START")
            val crit = Criteria().apply {
                accuracy = Criteria.ACCURACY_FINE
            }
            val provider = locationManager!!.getBestProvider(crit, true)
            //val loc: Location = provider?.let { locationManager!!.getLastKnownLocation(it) }!!
            Toast.makeText(this@MainActivity, "provider:$provider", Toast.LENGTH_SHORT).show()
            //lastGps()
            /*locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 25000, 1f, locationListener)
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 25000, 1f, locationListener)*/
            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, locationListener)
            }
            /*else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                    .setMessage("Включить GPS")
                    .setPositiveButton("Включить") { dialog, id ->
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, locationListener)
                    }
                val alert: AlertDialog = builder.create()
                alert.show()
            }*/
            if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0f, locationListener)
            }
            str += "------------------------------- \n"
        }else {
            chekPermissionGPS()
        }
    }

    private var netLoc: Location? = null
    private var gpsLoc:Location? = null
    private var gpsCoordinate:Location? = null
    @SuppressLint("MissingPermission")
    private fun lastGps(){
        val gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gpsEnabled) {
            gpsLoc = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        if (networkEnabled) {
            netLoc = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        if (gpsLoc != null && netLoc != null) {
            gpsCoordinate = if (gpsLoc!!.accuracy > netLoc!!.accuracy)
                netLoc;
            else
                gpsLoc;
        } else {

            if (gpsLoc != null) {
                gpsCoordinate = gpsLoc;
            } else if (netLoc != null) {
                gpsCoordinate = netLoc;
            }
        }
        str += "0)" + "${formatLocation(gpsCoordinate)} \n"
        binding.tvLocationGPS.text = str
    }

    private fun chekPermissionGPS(){
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
        ))
    }

    private var str = ""
    private var i = 1

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            str += "$i)" + "${location.latitude}, ${location.longitude} \n"
            binding.tvLocationGPS.text = str
            i++
            //Toast.makeText(this@MainActivity,"${location.latitude}, ${location.longitude}",Toast.LENGTH_SHORT).show()
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @SuppressLint("DefaultLocale")
    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else "${location.latitude}, ${location.longitude}"
    }
}