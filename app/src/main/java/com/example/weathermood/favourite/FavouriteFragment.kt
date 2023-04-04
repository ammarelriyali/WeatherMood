package com.example.weathermood.favourite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvm.DB.LocalDataClass
import com.example.weathermood.databinding.FragmentFavouriteBinding
import com.example.weathermood.favourite.mvvm.FavouriteViewFactory
import com.example.weathermood.favourite.mvvm.FavouriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
        val favouriteViewFactory = FavouriteViewFactory(LocalDataClass(requireContext()))
        val viewModel =
            ViewModelProvider(this, favouriteViewFactory).get(FavouriteViewModel::class.java)
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        binding.avEmptyLocation.playAnimation()
        binding.fabAddLocation.setOnClickListener() {
            val x = FavouriteFragmentDirections.actionNavFavouriteToMapsFragment()
            findNavController().navigate(x)
        }
        val adapter =FavouriteAdapter()
        binding.rvFavourite.apply {
            this.adapter=adapter
            this.layoutManager=GridLayoutManager(requireContext(),2)
        }


        viewModel.getFavItems()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.response.collect(){
                when(it){
                    is ResponseStateFav.Success -> {
                        if (it.data.isNullOrEmpty())
                            showAnimation()
                        else{
                            showList()
                            adapter.setData(it.data)
                        }
                    }
                    is ResponseStateFav.Failure ->  Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "We had issue pls try later",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> {}
                }
            }
        }
        return binding.root
    }

    private fun showList() {
        Log.i("TAG", "showList: ")
        binding.fabAddLocation.visibility=View.VISIBLE
        binding.rvFavourite.visibility=View.VISIBLE
        binding.avEmptyLocation.visibility=View.GONE
        binding.tvEmptyLocation.visibility=View.GONE
        binding.clShimmer.visibility=View.GONE
        binding.clFavourite.visibility=View.GONE
    }

    private fun showAnimation() {
        binding.fabAddLocation.visibility=View.VISIBLE
        binding.rvFavourite.visibility=View.GONE
        binding.avEmptyLocation.visibility=View.VISIBLE
        binding.tvEmptyLocation.visibility=View.VISIBLE
        binding.clShimmer.visibility=View.GONE
        binding.clFavourite.visibility=View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}