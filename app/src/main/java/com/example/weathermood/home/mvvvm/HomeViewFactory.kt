package com.example.weathermood.home.mvvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermood.home.mvvvm.repository.IRepositoryHome

class HomeViewFactory(private val repository: IRepositoryHome):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repository) as T
        } else
            throw java.lang.IllegalArgumentException("Wrong view Model")
    }
}