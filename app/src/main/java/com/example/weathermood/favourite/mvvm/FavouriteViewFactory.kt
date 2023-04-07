package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermood.favourite.mvvm.repository.RepositoryAlert

class FavouriteViewFactory(private val repositoryFavorite: RepositoryAlert):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repositoryFavorite) as T
        } else
            throw java.lang.IllegalArgumentException("Wrong view Model")
    }
}