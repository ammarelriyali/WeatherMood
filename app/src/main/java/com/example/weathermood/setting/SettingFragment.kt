package com.example.weathermood.setting

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.example.weathermood.R
import com.example.weathermood.databinding.FragmentSettingBinding
import com.example.weathermood.shareperf.MySharedPreference
import java.util.*

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

        when (MySharedPreference.getUnits()) {
            "kelvin" -> {
                binding.rbUnits.check(R.id.rb_kelvin)
            }
            "Celsius" -> {
                binding.rbUnits.check(R.id.rb_celsius)
            }
            "Fahrenheit" -> {
                binding.rbUnits.check(R.id.rb_fahrenheit)
            }
        }

        binding.rbLanguage.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i == R.id.rb_arbic && MySharedPreference.getLanguage()=="en") {
                MySharedPreference.setLanguage("ar")
                setLocal("ar")
            } else if (i == R.id.rb_english && MySharedPreference.getLanguage()=="ar") {
                MySharedPreference.setLanguage("en")
                setLocal("en")
            }
        }
        binding.rbLocation.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i == R.id.rb_maps)
                MySharedPreference.setWeatherFromMap(true)
            else
                MySharedPreference.setWeatherFromMap(false)
        }
        binding.rbUnits.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (i) {
                R.id.rb_kelvin -> {
                    MySharedPreference.setUnit("kelvin")
                }
                R.id.rb_celsius -> {
                    MySharedPreference.setUnit("Celsius")
                }
                R.id.rb_fahrenheit -> {
                    MySharedPreference.setUnit("Fahrenheit")
                }
            }
        }


        return binding.root
    }

    private fun setLocal(lang: String) {
        Toast.makeText(requireContext(), lang, Toast.LENGTH_LONG).show()
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        requireActivity().baseContext.resources.updateConfiguration(
            config,
            requireActivity().baseContext.resources.displayMetrics
        )

        // Determine layout direction based on selected language
        val layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

        // Set layout direction on the root view
        requireActivity().window.decorView.layoutDirection = layoutDirection
        this.requireActivity().recreate()
    }

}