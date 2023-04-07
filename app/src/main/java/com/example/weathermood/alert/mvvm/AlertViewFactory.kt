package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermood.favourite.mvvm.repository.RepositoryAlert

class AlertViewFactory(private val repositoryAlert: RepositoryAlert):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repositoryAlert) as T
        } else
            throw java.lang.IllegalArgumentException("Wrong view Model")
    }
}