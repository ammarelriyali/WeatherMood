package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.favourite.ResponseStateFav
import com.example.weathermood.favourite.mvvm.repository.IRepositoryFavorite
import com.example.weathermood.favourite.mvvm.repository.RepositoryFavorite
import com.example.weathermood.model.FavouriteLocation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavouriteViewModel(val repository: IRepositoryFavorite) : ViewModel() {
    private var _oneCall: MutableStateFlow<ResponseStateFav> =
        MutableStateFlow(ResponseStateFav.Loading)
    val response: MutableStateFlow<ResponseStateFav> = _oneCall

    fun getFavItems() {
        viewModelScope.launch {
            repository.getFavItems().catch { _oneCall.value = ResponseStateFav.Failure(it) }
                .collect() {
                    _oneCall.value = ResponseStateFav.Success(it)
                }
        }
    }

    fun delete(it: FavouriteLocation) {
        viewModelScope.launch {
            repository.deleteFavItem(it)
            getFavItems()
        }
    }

    fun insert(data: FavouriteLocation) {
        viewModelScope.launch {
            repository.insertFav(data)
        }
        getFavItems()
    }

    fun getCurrentWeather(
        favouriteLocation: FavouriteLocation,
    ) {
        viewModelScope.launch {
            repository.getCurrentLocation(
                favouriteLocation.longitude,
                favouriteLocation.latitude,
            )
                .catch { _oneCall.value = ResponseStateFav.Failure(it) }.collect() {
                   if (it.isSuccessful&& it.body() !=null)
                       _oneCall.value=ResponseStateFav.SuccessApi(it.body()!!)
                    else
                        _oneCall.value=ResponseStateFav.FailureResponse(it.code(),it.message())
                }
        }

    }
}