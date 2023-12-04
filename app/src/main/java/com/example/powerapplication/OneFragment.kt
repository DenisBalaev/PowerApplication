package com.example.powerapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.FragmentOneBinding
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.CancellationException

class OneFragment : Fragment(R.layout.fragment_one) {
    private val binding by viewBinding(FragmentOneBinding::bind)
    private val TAG = "TEST_DEBUG_COROUTINE"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val handler = CoroutineExceptionHandler {_, exception ->
            Log.d(TAG,"got $exception")
        }
        //Продолжает работать
        lifecycleScope.launch (handler) {
           repeat(20) {
                try {
                    Log.d(TAG, "LSCOPE: lifecycleScope started  $it")
                    delay(5000)
                    Log.d(TAG, "LSCOPE: lifecycleScope completed  $it")
                } catch (e: CancellationException) {
                    Log.d(TAG, "LSCOPE: lifecycleScope cancelled: $e")
                }
            }
        }

        //Падает в ERROR
        viewLifecycleOwner.lifecycleScope.launch (handler) {
            repeat(20) {
                try {
                    Log.d(TAG,"LSCOPE: viewLifecycleOwner.lifecycleScope started")
                    delay(5000)
                    Log.d(TAG, "LSCOPE: viewLifecycleOwner.lifecycleScope completed")
                }
                catch(e: CancellationException){
                    Log.d(TAG,"LSCOPE: viewLifecycleOwner.lifecycleScope cancelled: $e")
                }

                Timber.tag("CAMERA_INFORMATION_TEST").d("Restarting camera")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repeat(20) {
                try {
                    Log.d(TAG, "LSCOPE: CoroutineScope started $it")
                    delay(5000)
                    Log.d(TAG, "LSCOPE: CoroutineScope completed  $it")
                } catch (e: CancellationException) {
                    Log.d(TAG, "LSCOPE: CoroutineScope cancelled: $e  $it")
                }
            }
        }

        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.twoFragment)
        }
    }
}