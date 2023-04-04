package com.example.weathermood.favourite.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.DB.LocalData
import com.example.weathermood.favourite.ResponseStateFav
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteViewModel(val localData:LocalData) : ViewModel() {
    private var _oneCall: MutableStateFlow<ResponseStateFav> = MutableStateFlow(ResponseStateFav.Loading)
    val response: MutableStateFlow<ResponseStateFav> = _oneCall

    fun getFavItems() {
        viewModelScope.launch {
        localData.getFavItems().catch { _oneCall.value=ResponseStateFav.Failure(it) }
            .collect(){
               _oneCall.value=ResponseStateFav.Success(it)
            }
        }
    }


}