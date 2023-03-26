package com.example.weathermood.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

    private val My_LOCATION_PERMISSION_ID: Int = 33
    private val TAG: String = "TAGG"
    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedClient: FusedLocationProviderClient

    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myFactory = HomeViewFactory(Repository(LocalDataClass(requireContext()), Serves()))
        viewModel = ViewModelProvider(this, myFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());



        viewModel.response.observe(requireActivity()
        ) {
            if (it.isSuccessful&&binding != null) {
                Log.i(TAG, "onCreateView: xxx")
                    binding.tvTimeHome.text = getDateTime(it.body()?.current!!.dt)
            } else
                Snackbar.make(
                    binding.root,
                    "server is busy try again later pls",
                    Snackbar.LENGTH_SHORT
                ).show()
            if (binding!=null) {
                binding.shimmerHome.stopShimmer()
                binding.shimmerHome.visibility = View.GONE
                binding.clHome.visibility = View.VISIBLE
            }
        }


        getLocation()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.clHome.visibility=View.GONE
        binding.shimmerHome.visibility=View.VISIBLE
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
        } else
            askForPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun getLocationData() {
        fusedClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token
                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT)
                        .show()
                else {
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
    private fun getDateTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("hh.mm a")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}