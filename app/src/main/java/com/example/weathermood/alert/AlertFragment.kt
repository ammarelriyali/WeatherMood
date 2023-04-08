package com.example.weathermood.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.Serves
import com.example.weathermood.alert.mvvm.AlertViewFactory
import com.example.weathermood.alert.mvvm.AlertViewModel
import com.example.weathermood.alert.mvvm.repository.RepositoryAlert
import com.example.weathermood.databinding.FragmentAlertBinding
import com.example.weathermood.utilities.Helper


class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    lateinit var viewModel: AlertViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val favouriteViewFactory =
            AlertViewFactory(RepositoryAlert(LocalDataClass(requireContext()), Serves()))
        viewModel =
            ViewModelProvider(this, favouriteViewFactory)[AlertViewModel::class.java]
        binding.fabAddAlert.setOnClickListener(){
            showDialog()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {
        val dialogFragment = MyAlertDialog()
        dialogFragment.isCancelable = false
        dialogFragment.show(getParentFragmentManager(), "MyDialogFragment",)
    }
}