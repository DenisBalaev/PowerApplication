package com.example.powerapplication

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.ActivityKeyBinding
import kotlinx.coroutines.*

//dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, key))

class KeyActivity : AppCompatActivity(R.layout.activity_key) {

    val binding by viewBinding(ActivityKeyBinding::bind)

    private val key = KeyEvent.KEYCODE_POWER
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            repeat(4){
                onKeyLongPress(key,KeyEvent(KeyEvent.ACTION_DOWN, key))
                delay(3000)
            }
        }
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == key) {
            event!!.startTracking()
            Log.d("Test", "Long press! $keyCode")
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("Test", "Click btn code:! $keyCode \n $event")
        if (keyCode == key) {
            event.startTracking()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }*/
}