package com.example.weathermood.map

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mvvm.DB.LocalData
import com.example.mvvm.DB.LocalDataClass
import com.example.weathermood.R
import com.example.weathermood.model.FavouriteLocation

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MapsFragment : Fragment() {
    val args: MapsFragmentArgs by navArgs()
    lateinit var button: Button
    private var position: LatLng = LatLng(0.0,0.0)

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnMapClickListener {
            position = it //get lag lot
            button.text = getString(R.string.pick)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_maps, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        button = view.findViewById(R.id.b_pick)
        button.setOnClickListener() {
           navigate()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigate()
        }
    }


    private fun navigate() {
        Log.i("TAGG", "navigate: ${position.latitude} ${position.longitude}")
        if (args.isHome) {
            val x = MapsFragmentDirections.actionMapsFragmentToNavHome(
            ).setLat(position!!.latitude.toString()).setLog(position!!.longitude.toString()).setIsOpen(true)
            findNavController().navigate(x)
        } else {
            if (position.latitude != 0.0)
                insertFavItem()
            val x = MapsFragmentDirections.actionMapsFragmentToNavFavourite(
            ).setLat(position!!.latitude.toString()).setLog(position!!.longitude.toString())
            findNavController().navigate(x)
        }
    }

    private fun insertFavItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            val local:LocalData=LocalDataClass(requireContext())
            val city=getCityName()
            local.insertFav(FavouriteLocation(position!!.longitude.toString(),position!!.latitude.toString(),city))
        }

    }

    private fun getCityName() :String{
        var city=""
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address: MutableList<Address>? =
                geocoder.getFromLocation(position.latitude, position.longitude, 1)
              city = address?.get(0)?.getAddressLine(0)!!

        }
        return city
    }

}