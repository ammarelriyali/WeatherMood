package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.DB.LocalData

class FavouriteViewFactory(private val local: LocalData):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(local) as T
        } else
            throw java.lang.IllegalArgumentException("Wrong view Model")
    }
}