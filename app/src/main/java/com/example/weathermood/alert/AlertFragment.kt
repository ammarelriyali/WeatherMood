package com.example.weathermood.alert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.Serves
import com.example.weathermood.alert.mvvm.AlertViewFactory
import com.example.weathermood.alert.mvvm.AlertViewModel
import com.example.weathermood.alert.mvvm.ResponseStateAlert
import com.example.weathermood.alert.mvvm.repository.RepositoryAlert
import com.example.weathermood.databinding.FragmentAlertBinding
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {

    private val TAG ="TAGG"
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

        val adapter=AdapterAlert(onDelete = {viewModel.delete(it)})
        binding.rvAlert.apply {
            this.adapter=adapter
            layoutManager=LinearLayoutManager(requireContext())
        }

        setFragmentResultListener("") { s: String, bundle: Bundle ->
            viewModel.getItems()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.response.collect(){
                when(it){
                    is ResponseStateAlert.Success ->{
                        if (it.data.isNullOrEmpty())
                            showAnimation()
                        else{
                            showList()
                            adapter.setList(it.data)
                        }
                    }
                    is ResponseStateAlert.Failure ->{
                        Log.i(TAG, "onCreateView: ${it.msg}")
                        showAnimation()
                    }
                    else ->{}
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getItems()

    }
    private fun showList() {
        binding.avEmptyAlert.visibility=View.GONE
        binding.avEmptyAlert.cancelAnimation()
        binding.rvAlert.visibility=View.VISIBLE
    }

    private fun showAnimation() {
        binding.avEmptyAlert.visibility=View.VISIBLE
        binding.avEmptyAlert.playAnimation()
        binding.rvAlert.visibility=View.GONE
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