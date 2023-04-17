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
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.RemotelyDataSource
import com.example.weathermood.databinding.FragmentHomeBinding
import com.example.weathermood.home.HomeFragmentDirections.actionNavHomeToMapsFragment
import com.example.weathermood.home.mvvvm.HomeViewFactory
import com.example.weathermood.home.mvvvm.HomeViewModel
import com.example.weathermood.home.mvvvm.ResponseStateHome
import com.example.weathermood.home.mvvvm.repository.RepositoryHome
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import com.example.weathermood.shareperf.MySharedPreference
import com.example.weathermood.utilities.Helper
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

import java.util.*

class HomeFragment : Fragment() {

    private lateinit var lon: String
    private lateinit var lat: String
    val args: HomeFragmentArgs by navArgs()
    private var city: String? = null
    private val My_LOCATION_PERMISSION_ID: Int = 33
    private val TAG: String = "TAGG"
    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedClient: FusedLocationProviderClient
companion object{
    var isNotOpen:Boolean=true
}

    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel


    private val dailyAdapter = DailyAdapter()
    private val hourlyAdapter = HourlyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val myFactory = HomeViewFactory(RepositoryHome(LocalDataClass(requireContext()), RemotelyDataSource()))

        viewModel = ViewModelProvider(this, myFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        try {
            binding.rvHourlyWeatherHome.apply {
                adapter = hourlyAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }
            binding.rvDaliyWeatherHome.apply {
                adapter = dailyAdapter
                layoutManager = object : LinearLayoutManager(requireContext()) {
                    override fun canScrollVertically() = false
                }
            }
            binding.ivRefreshHome.setOnClickListener() {
                binding.progressBar.visibility = View.VISIBLE
                binding.ivRefreshHome.visibility = View.GONE
                handleIsOnlineState()
            }

            binding.llLoctionName.setOnClickListener() {
                if (isOnline()){
                showDialog()
                }else
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "pls connect to internet",
                        Snackbar.LENGTH_LONG
                    ).show()

            }
        }catch (e:java.lang.Exception){
            Log.i(TAG, "onCreateView: ${e.message}")
        }
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.response.collect() {
                when (it) {
                    is ResponseStateHome.SuccessApi -> {
                       try {
                            setData(it.data)}
                       catch (e:java.lang.Exception){
                           Log.i(TAG, "onCreateView: $e")
                       }
                        viewModel.insertCall(OneCallHome(oneCall = it.data).apply {
                            this.city = this@HomeFragment.city ?: "Empty"
                        })
                        disableShimmer()
                    }
                    is ResponseStateHome.Success -> {
                        this@HomeFragment.city = it.data.city
                        this@HomeFragment.lat = it.data.oneCall.lat.toString()
                        this@HomeFragment.lon = it.data.oneCall.lon.toString()

                       try {
                            setData(it.data.oneCall)}
                       catch (e:java.lang.Exception){
                           Log.i(TAG, "onCreateView: ${e.message}")
                       }
                        disableShimmer()
                    }
                    is ResponseStateHome.Failure -> {
                        Log.i(TAG, "onCreate: ${it.msg}")
                        disableShimmer()
                    }
                    is ResponseStateHome.FailureHomeResponse -> {
                        Log.i(TAG, "onCreate: ${it.data}  :  ${it.msg}")
                        Log.i(TAG, "onCreate: ${it.data}  :  ${it.msg}")
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "the server had issue pls try later",
                            Snackbar.LENGTH_LONG
                        ).show()
                        disableShimmer()
                    }
                    else -> {
                        enableShimmer()
                    }
                }
            }

        }

        setFragmentRes()

        enableShimmer()

        return binding.root
    }

    private fun handleIsOnlineState() {
        if (isOnline()) {
            viewModel.getCurrentWeather(lon, lat)
            getCityName()
        } else {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "please open internet",
                Snackbar.LENGTH_LONG
            ).show()
            disableShimmer()
        }

    }

    override fun onResume() {
        super.onResume()
        if (MySharedPreference.isFirstTime()) {
            showDialog()
            MySharedPreference.setFirstTime()
        } else if (args.isOpen &&isNotOpen) {
            if (args.lat != "0.0") {
                lat = args.lat
                lon = args.log
                isNotOpen=false
                handleIsOnlineState()
            }
        } else if (MySharedPreference.getWeatherFromMap()) {
            navigationFromHomeToMap()
            isNotOpen=true
        } else{
            getLocation()
        }
        viewModel.getWeather()
    }

    private fun setFragmentRes() {
        setFragmentResultListener(Helper.REQUEST_KEY_MAPS) { s: String, b: Bundle ->
            MySharedPreference.setWeatherFromMap(true)
            navigationFromHomeToMap()
        }
        setFragmentResultListener(Helper.REQUEST_KEY_GPS) { s: String, b: Bundle ->
            MySharedPreference.setWeatherFromMap(false)
            Log.i(TAG, "setFragmentRes: ------")
            getLocation()

        }
    }

    private fun navigationFromHomeToMap() {
        val x = actionNavHomeToMapsFragment().setIsHome(true)
        findNavController().navigate(x)
    }

    private fun showDialog() {
        enableShimmer()
        isNotOpen=true
        val dialogFragment = MyHomeDialog()
        dialogFragment.isCancelable = false
        dialogFragment.show(getParentFragmentManager(), "MyDialogFragment")
    }

    private fun setData(data: OneCall) {
        binding.tvLocationHome.text=city
        binding.tvDateHome.text = getDate(data.current!!.dt)
        binding.tvTempHome.text =
            data.current!!.temp.toString() + '\u00B0'.toString() + when (MySharedPreference.getUnits()) {
                "metric" -> "C"
                "imperial" -> "F"
                else -> "K"
            }
        binding.tvStatusHome.text = data.current!!.weather[0].description
        binding.tvLastUpdateHome.text =
            getString(com.example.weathermood.R.string.lastUpdate) +" "+ getTime(data.current!!.dt)

        dailyAdapter.setData(data.daily!!)
        hourlyAdapter.setData(data.hourly!!)
        Glide.with(requireContext()).load(
            AppCompatResources.getDrawable(
                requireContext(),
                Helper.image.get(data.current!!.weather[0].icon)!!
            )
        )
            .error(
                AppCompatResources.getDrawable(
                    requireContext(),
                    com.example.weathermood.R.drawable.twotone_error_24
                )
            )
            .into(binding.ivIconHome)
        binding.tvHumidityHome.text = data.current?.humidity.toString() + " %"
        val unit = getUnit()
        binding.tvWindSpeedHome.text =
            data.current?.wind_speed.toString() + unit //check about the وحده
        binding.tvPressureHome.text =
            data.current?.pressure.toString() + if (MySharedPreference.getLanguage() == "en") "hPa" else "بار"
        binding.tvCloudsHome.text = data.current?.clouds.toString() + " %"
    }

    private fun getUnit(): String = when (MySharedPreference.getLanguage()) {
        "en" -> when (MySharedPreference.getUnits()) {
            "imperial" -> " m/h"
            else -> " m/s"
        }
        else -> when (MySharedPreference.getUnits()) {
            "imperial" -> " م/س"
            else -> " م/ث"
        }
    }

    private fun enableShimmer() {
        try {
            binding.clHome.visibility = View.GONE
            binding.shimmerHome.visibility = View.VISIBLE
            binding.shimmerHome.startShimmer()
        } catch (e:java.lang.Exception) {
            Log.i(TAG, "enableShimmer: $e")
        }
    }

    private fun disableShimmer() {
        try{
            binding.shimmerHome.stopShimmer()
            binding.shimmerHome.visibility = View.GONE
            binding.clHome.visibility = View.VISIBLE
            binding.ivRefreshHome.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }catch (e:java.lang.Exception) {
            Log.i(TAG, "enableShimmer: $e")
        }
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
        enableShimmer()
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
                       lon= location.longitude.toString()
                       lat= location.latitude.toString()
                        handleIsOnlineState()
                    } catch (e: java.lang.Exception) {
                        Log.i(TAG, "getLocationData: gec ${e.message}")
                        withContext(Dispatchers.Main) {
                            disableShimmer()
                        }
                    }
                }

            }
        }

    }

    private fun getCityName() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCityName: $lat $lon")
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address: MutableList<Address>? =
                geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
            if (address.isNullOrEmpty() || address.get(0).getAddressLine(0) == null)
                city = "empty"
            else {
                val arr = address?.get(0)?.getAddressLine(0)!!.split(",")
                if (arr.size > 1)
                    city = arr[1]
                else
                    city = arr[0]
            }
            withContext(Dispatchers.Main) {
                binding.tvLocationHome.text = city
            }
        }
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
        Log.i(TAG, "onRequestPermissionsResult: -------")
        if (requestCode == My_LOCATION_PERMISSION_ID)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: ")

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

    private fun isOnline(): Boolean {
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

