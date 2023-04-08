package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermood.favourite.mvvm.repository.RepositoryFavorite

class FavouriteViewFactory(private val repositoryFavorite: RepositoryFavorite):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(repositoryFavorite) as T
        } else
            throw java.lang.IllegalArgumentException("Wrong view Model")
    }
}