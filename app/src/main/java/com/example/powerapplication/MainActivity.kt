package com.example.powerapplication

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("tagPower","MainActivity")

        /*val manufacturer = "xiaomi"
        if (manufacturer.equals(Build.MANUFACTURER, ignoreCase = true)) {
            //this will open auto start screen where user can enable permission for your app
            val intent = Intent()
            intent.component = ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
            startActivity(intent)
        }*/
    }
}