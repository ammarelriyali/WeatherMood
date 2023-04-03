package com.example.weathermood.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.weathermood.databinding.FragmentDailogHomeBinding
import com.example.weathermood.utilities.Helper

class MyHomeDialog : DialogFragment() {
    lateinit var _binding: FragmentDailogHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailogHomeBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.llMaps.setOnClickListener {
            Log.i("TAG", "onViewCreated: maps")
            setFragmentResult(Helper.REQUEST_KEY_MAPS, Bundle())
            dismiss()
        }
        _binding.llGps.setOnClickListener{
            Log.i("TAG", "onViewCreated: gps")
            setFragmentResult(Helper.REQUEST_KEY_GPS,Bundle())
            dismiss()
        }
    }

}