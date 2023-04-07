package com.example.weathermood.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.example.weathermood.R
import com.example.weathermood.databinding.FragmentSettingBinding
import com.example.weathermood.shareperf.MySharedPreference

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        if (MySharedPreference.getWeatherFromMap())
            binding.rbLocation.check(R.id.rb_maps)
        else
            binding.rbLocation.check(R.id.rb_gps)

        if (MySharedPreference.getLanguage() == "en")
            binding.rbLanguage.check(R.id.rb_english)
        else
            binding.rbLanguage.check(R.id.rb_arbic)

        when (MySharedPreference.getUnits()){
            "kelvin" -> {binding.rbUnits.check(R.id.rb_kelvin)}
            "Celsius" -> {binding.rbUnits.check(R.id.rb_celsius)}
            "Fahrenheit" -> {binding.rbUnits.check(R.id.rb_fahrenheit)}
        }

        binding.rbLanguage.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i==R.id.rb_arbic)
                MySharedPreference.setLanguage("ar")
            else
                MySharedPreference.setLanguage("en")
        }
        binding.rbLocation.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i==R.id.rb_maps)
                MySharedPreference.setWeatherFromMap(true)
            else
                MySharedPreference.setWeatherFromMap(false)
        }
        binding.rbUnits.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when(i){
                R.id.rb_kelvin ->{MySharedPreference.setUnit("kelvin")}
                R.id.rb_celsius ->{MySharedPreference.setUnit("Celsius")}
                R.id.rb_fahrenheit ->{MySharedPreference.setUnit("Fahrenheit")}
            }
        }


        return binding.root
    }


}