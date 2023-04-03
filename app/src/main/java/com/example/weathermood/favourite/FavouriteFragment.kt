package com.example.weathermood.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.DB.LocalDataClass
import com.example.weathermood.databinding.FragmentFavouriteBinding
import com.example.weathermood.favourite.mvvm.FavouriteViewFactory
import com.example.weathermood.favourite.mvvm.FavouriteViewModel

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favouriteViewFactory= FavouriteViewFactory(LocalDataClass(requireContext()))
        val viewModel =
            ViewModelProvider(this,favouriteViewFactory).get(FavouriteViewModel::class.java)
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        binding.avEmptyLocation.playAnimation()


        binding.fabAddLocation.setOnClickListener(){
            
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}