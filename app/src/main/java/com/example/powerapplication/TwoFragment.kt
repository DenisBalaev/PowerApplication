package com.example.powerapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.powerapplication.databinding.FragmentOneBinding
import com.example.powerapplication.databinding.FragmentTwoBinding


class TwoFragment : Fragment(R.layout.fragment_two) {
    private val binding by viewBinding(FragmentTwoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.oneFragment)
        }
    }
}