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
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.Serves
import com.example.weathermood.databinding.FragmentHomeBinding
import com.example.weathermood.home.mvvvm.HomeViewFactory
import com.example.weathermood.home.mvvvm.HomeViewModel
import com.example.weathermood.home.repository.Repository
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import com.example.weathermood.utilities.Helper
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

import java.util.*

class HomeFragment : Fragment() {

    private var city: String? = null
    private val My_LOCATION_PERMISSION_ID: Int = 33
    private val TAG: String = "TAGG"
    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedClient: FusedLocationProviderClient


    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel


    private val dailyAdapter = DailyAdapter()
    private val hourlyAdapter = HourlyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myFactory = HomeViewFactory(Repository(LocalDataClass(requireContext()), Serves()))
        viewModel = ViewModelProvider(this, myFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (!checkPermissions())
            askForPermissions()

        binding.rvHourlyWeatherHome.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        binding.rvDaliyWeatherHome.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.ivRefreshHome.setOnClickListener(){
            binding.progressBar.visibility=View.VISIBLE
            binding.ivRefreshHome.visibility=View.GONE
            getLocationOnline()
        }
        lifecycleScope.launchWhenCreated {
            viewModel.response.collect() {
                when (it) {
                    is ResponseState.SuccessApi -> {
                        setData(it.data)
                        viewModel.insertCall(OneCallHome(oneCall = it.data).apply {
                            this.city= this@HomeFragment.city ?: "Empty"
                        })
                        disableShimmer()
                    }
                    is ResponseState.Success -> {
                        this@HomeFragment.city=it.data.city
                        setData(it.data.oneCall)
                        disableShimmer()
                    }
                    is ResponseState.Failure -> {
                        Log.i(TAG, "onCreate: ${it.msg}")
                        disableShimmer()
                    }
                    is ResponseState.FailureResponse -> {
                        Log.i(TAG, "onCreate: ${it.data}  :  ${it.msg}")
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),"the server had issue pls try later",Snackbar.LENGTH_LONG).show()
                        disableShimmer()
                    }
                    else -> {
                        enableShimmer()
                    }
                }
            }
        }

        return binding.root
    }

    private fun getLocationOnline() {
        if (isOnline())
            getLocation()
        else{
            Snackbar.make(requireActivity().findViewById(android.R.id.content),"please open internet",Snackbar.LENGTH_LONG).show()
            Log.i(TAG, "getLocationOnline: ")
            disableShimmer()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getWeather()
        getLocationOnline()
    }

    private fun setData(data: OneCall) {

        binding.tvLocationHome.text = city

        binding.tvDateHome.text = getDate(data.current!!.dt)
        binding.tvTempHome.text =
            data.current!!.temp.toString() + '\u00B0'.toString() + "K"
        binding.tvStatusHome.text = data.current!!.weather[0].description
        binding.tvLastUpdateHome.text =
            "last update " + getTime(data.current!!.dt)

        dailyAdapter.setData(data.daily!!)
        hourlyAdapter.setData(data.hourly!!)
        Glide.with(requireContext()).load(
            AppCompatResources.getDrawable(requireContext(),
                Helper.image.get(data.current!!.weather[0].icon)!!
            )
        )
            .error(AppCompatResources.getDrawable(requireContext(),com.example.weathermood.R.drawable.twotone_error_24))
            .into(binding.ivIconHome)
        binding.tvHumidityHome.text = data.current?.humidity.toString() + " %"
        binding.tvWindSpeedHome.text =
            data.current?.wind_speed.toString() + " m/s"  //check about the وحده
        binding.tvPressureHome.text = data.current?.pressure.toString() + " hPa"
        binding.tvCloudsHome.text = data.current?.clouds.toString() + " %"
    }

    private fun enableShimmer() {
        binding.clHome.visibility = View.GONE
        binding.shimmerHome.visibility = View.VISIBLE
        binding.shimmerHome.startShimmer()


    }

    private fun disableShimmer() {
        binding.shimmerHome.stopShimmer()
        binding.shimmerHome.visibility = View.GONE
        binding.clHome.visibility = View.VISIBLE
        binding.ivRefreshHome.visibility= View.VISIBLE
        binding.progressBar.visibility= View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        } else
            askForPermissions()
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
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val address: MutableList<Address>? =
                            location?.let { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
                        city = address?.get(0)?.getAddressLine(0)!!.split(",")[1]
                        setLocation(location.latitude.toString(), location.longitude.toString())
                    }catch (e:java.lang.Exception){
                        Log.i(TAG, "getLocationData: ${e.message}")
                    }
                }

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
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            My_LOCATION_PERMISSION_ID
        )
    }

    private fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
    }

    private fun getTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("h.m a")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun getDate(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd MMMM YYYY")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
    fun isOnline(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}