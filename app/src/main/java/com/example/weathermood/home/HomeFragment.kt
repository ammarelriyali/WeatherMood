package com.example.weathermood.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.Serves
import com.example.weathermood.databinding.FragmentHomeBinding
import com.example.weathermood.home.mvvvm.HomeViewFactory
import com.example.weathermood.home.mvvvm.HomeViewModel
import com.example.weathermood.home.repository.Repository
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {

    private var city: String? = null
    private val My_LOCATION_PERMISSION_ID: Int = 33
    private val TAG: String = "TAGG"
    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedClient: FusedLocationProviderClient


    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val myFactory = HomeViewFactory(Repository(LocalDataClass(requireContext()), Serves()))
        viewModel = ViewModelProvider(this, myFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        viewModel.response.observe(
            requireActivity()
        ) {
            if (it.isSuccessful && binding != null) {
                Log.i(TAG, "onCreateView: xxx")
                if (it.body()?.city.equals("Empty"))
                    it.body()?.city = city ?: "Empty"
                binding.tvLocationHome.text = it.body()!!.city
                binding.tvDayConditionHome.text =
                    getCondition(it.body()?.current?.sunrise!!, it.body()?.current?.sunset!!)
                binding.tvDateHome.text = getDate(it.body()?.current!!.dt)
                binding.tvTempHome.text =
                    it.body()?.current!!.temp.toString() + '\u00B0'.toString() + "K"
                binding.tvStatusHome.text = it.body()?.current!!.weather[0].main
                binding.tvLastUpdateHome.text = "last update " + getTime(it.body()?.current!!.dt)

            } else Snackbar.make(
                binding.root, "server is busy try again later pls", Snackbar.LENGTH_SHORT
            ).show()
            if (binding != null) {
                binding.shimmerHome.stopShimmer()
                binding.shimmerHome.visibility = View.GONE
                binding.clHome.visibility = View.VISIBLE
                binding.ivRefreshHome.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }

        if (binding != null) {
            binding.ivRefreshHome.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                binding.ivRefreshHome.visibility = View.GONE
                getLocation()
            }
        }
        getLocation()
        return binding.root
    }

    private fun getCondition(sunrise: Long, sunset: Long): String {
        var res = "Night"
        try {
            val cmpSunrise = Date().compareTo(getTimeDate(sunrise))
            val cmpSunset = Date().compareTo(getTimeDate(sunset))
            res = when {
                cmpSunrise > 0 && cmpSunset < 0 -> "Morning"
                else -> "Night"
            }
        } catch (e: java.lang.Exception) {
            Log.i(TAG, "getCondition: " + e.message)
        }
        return res
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.clHome.visibility = View.GONE
        binding.shimmerHome.visibility = View.VISIBLE
        binding.shimmerHome.startShimmer()
    }

    private fun enableLocation(): Boolean {
        val location: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return location.isProviderEnabled(LocationManager.GPS_PROVIDER) || location.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (enableLocation()) {
                getLocationData()
            } else {
                Toast.makeText(context, "enable location ", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

            }
        } else askForPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun getLocationData() {
        fusedClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location: Location? ->
            if (location == null) Toast.makeText(
                requireContext(),
                "Cannot get location.",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val address: MutableList<Address>? =
                    location?.let { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
                city = address?.get(0)?.countryName
                setLocation(location!!.latitude.toString(), location!!.longitude.toString())
            }
        }
    }


    private fun setLocation(lat: String, lon: String) {
        viewModel.getCurrentWeather(lon, lat)
        Log.i(TAG, "onLocationResult: $lat")
        Log.i(TAG, "onLocationResult: $lon")

    }

    private fun askForPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), My_LOCATION_PERMISSION_ID
        )
    }

    private fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID) if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        }
    }

    private fun getTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("hh.mm a")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun getTimeDate(s: Long): Date? {
        try {
            val netDate = Date(s * 1000)
            return netDate
        } catch (e: Exception) {
            Log.i(TAG, "getTimeDate: " + e.message)
            return null

        }
    }

    private fun getDate(s: Long): String? {
        try {
            Log.i(TAG, "getDate: " + s)
            val sdf = SimpleDateFormat("d MMMM YYYY ")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}