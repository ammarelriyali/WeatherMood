package com.example.weathermood.favourite

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.Serves
import com.example.weathermood.R
import com.example.weathermood.databinding.FragmentFavouriteBinding
import com.example.weathermood.favourite.mvvm.FavouriteViewFactory
import com.example.weathermood.favourite.mvvm.FavouriteViewModel
import com.example.weathermood.favourite.mvvm.repository.RepositoryFavorite
import com.example.weathermood.home.DailyAdapter
import com.example.weathermood.home.HourlyAdapter
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.utilities.Helper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FavouriteFragment : Fragment() {

    private lateinit var data: FavouriteLocation
    private var _binding: FragmentFavouriteBinding? = null
    private val dailyAdapter = DailyAdapter()
    private val hourlyAdapter = HourlyAdapter()
    private var city: String = "empty"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var viewModel:FavouriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favouriteViewFactory =
            FavouriteViewFactory(RepositoryFavorite(LocalDataClass(requireContext()), Serves()))
        viewModel =
            ViewModelProvider(this, favouriteViewFactory).get(FavouriteViewModel::class.java)
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        binding.avEmptyLocation.playAnimation()
        binding.fabAddLocation.setOnClickListener() {
            val x = FavouriteFragmentDirections.actionNavFavouriteToMapsFragment()
            findNavController().navigate(x)
        }
        val adapter = FavouriteAdapter(onDelete = { viewModel.delete(it);showSnack(it) },
            onCLick = { handleIsOnlineState(it);data = it; showShimmer() })
        binding.rvFavourite.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }


        viewModel.getFavItems()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.response.collect() {
                when (it) {
                    is ResponseStateFav.Success -> {
                        if (it.data.isNullOrEmpty())
                            showAnimation()
                        else {
                            showList()
                            adapter.setList(it.data.sortedBy { it.city })
                        }
                    }
                    is ResponseStateFav.Failure -> Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.weHadIssue),
                        Snackbar.LENGTH_LONG
                    ).show()
                    is ResponseStateFav.SuccessApi -> {
                        setData(it.data)
                        showWeather()
                    }
                    is ResponseStateFav.FailureResponse -> {
                        Log.i("TAG", "onCreateView: ${it.data} ${it.msg}")
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.serverHadIssue),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> {}
                }
            }
        }
        binding.rvHourlyWeatherFavourite.apply {
            this.adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        binding.rvDaliyWeatherFavourite.apply {
            this.adapter = dailyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.llLocationFavourite.setOnClickListener() {
            showList()
        }
        binding.ivRefreshFavourite.setOnClickListener() {
            binding.ivRefreshFavourite.visibility = View.GONE
            binding.progressBarFav.visibility = View.VISIBLE
            handleIsOnlineState(data)
        }
        return binding.root
    }

    private fun showSnack(data: FavouriteLocation) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            "item delete ",
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) { viewModel.insert(data) }.show()
    }

    private fun showShimmer() {
        binding.shimmerFavourite.visibility = View.VISIBLE
        binding.shimmerFavourite.startShimmer()
        binding.fabAddLocation.visibility = View.GONE
        binding.rvFavourite.visibility = View.GONE
        binding.avEmptyLocation.visibility = View.GONE
        binding.tvEmptyLocation.visibility = View.GONE
        binding.clFavourite.visibility = View.GONE
    }

    private fun showWeather() {
        binding.fabAddLocation.visibility = View.GONE
        binding.rvFavourite.visibility = View.GONE
        binding.avEmptyLocation.visibility = View.GONE
        binding.tvEmptyLocation.visibility = View.GONE
        binding.clFavourite.visibility = View.VISIBLE
        binding.shimmerFavourite.visibility = View.GONE
        binding.shimmerFavourite.stopShimmer()
        binding.progressBarFav.visibility = View.GONE
        binding.ivRefreshFavourite.visibility = View.VISIBLE
    }

    private fun showList() {
        binding.fabAddLocation.visibility = View.VISIBLE
        binding.rvFavourite.visibility = View.VISIBLE
        binding.avEmptyLocation.visibility = View.GONE
        binding.tvEmptyLocation.visibility = View.GONE
        binding.clFavourite.visibility = View.GONE
        binding.shimmerFavourite.visibility = View.GONE
        binding.shimmerFavourite.stopShimmer()
    }

    private fun showAnimation() {
        binding.fabAddLocation.visibility = View.VISIBLE
        binding.rvFavourite.visibility = View.GONE
        binding.avEmptyLocation.visibility = View.VISIBLE
        binding.tvEmptyLocation.visibility = View.VISIBLE
        binding.clFavourite.visibility = View.GONE
        binding.shimmerFavourite.visibility = View.GONE
        binding.shimmerFavourite.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: OneCall) {
        binding.tvLocationFavourite.text = city

        binding.tvDateFavourite.text = getDate(data.current!!.dt)
        binding.tvTempFavourite.text =

            data.current!!.temp.toString() + '\u00B0'.toString() + "K"
        binding.tvStatusFavourite.text = data.current!!.weather[0].description
        binding.tvLastUpdateFavourite.text =
            getString(R.string.lastUpdate) + getTime(data.current!!.dt)

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
            .into(binding.ivIconFavourite)
        binding.tvHumidityFavourite.text = data.current?.humidity.toString() + " %"
        binding.tvWindSpeedFavourite.text =
            data.current?.wind_speed.toString() + " m/s"  //check about the وحده
        binding.tvPressureFavourite.text = data.current?.pressure.toString() + " hPa"
        binding.tvCloudsFavourite.text = data.current?.clouds.toString() + " %"
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

    private fun handleIsOnlineState(data: FavouriteLocation) {
        if (isOnline()) {
            viewModel.getCurrentWeather(data)
            city = data.city
        } else {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "please open internet",
                Snackbar.LENGTH_LONG
            ).show()
            showList()
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