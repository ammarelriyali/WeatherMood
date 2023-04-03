package com.example.weathermood.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weathermood.R

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    val args: MapsFragmentArgs by navArgs()
    lateinit var button: Button
    private var postion: LatLng? = null
    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnMapClickListener {
            postion = it //get lag lot
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
            if (postion != null) {
                if (args.isHome) {
                    val x = MapsFragmentDirections.actionMapsFragmentToNavHome(
                        postion!!.latitude.toString(),
                        postion!!.longitude.toString()
                    )
                    findNavController().navigate(x)
                } else {
                    val x = MapsFragmentDirections.actionMapsFragmentToNavFavourite(
                        postion!!.latitude.toString(),
                        postion!!.longitude.toString()
                    )
                    findNavController().navigate(x)
                }
            } else
                Log.i("TAG", "onViewCreated: maps")
        }

    }
}