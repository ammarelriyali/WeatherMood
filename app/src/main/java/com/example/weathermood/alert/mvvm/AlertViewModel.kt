package com.example.weathermood.alert.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.alert.mvvm.repository.RepositoryAlert
import com.example.weathermood.model.AlertModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AlertViewModel(private val repository: RepositoryAlert) : ViewModel() {
    private var _oneCall: MutableStateFlow<ResponseStateAlert> =
        MutableStateFlow(ResponseStateAlert.Loading)
    val response: MutableStateFlow<ResponseStateAlert> = _oneCall
    fun getItems() {
        viewModelScope.launch {
            repository.getAlertItems().catch { _oneCall.value = ResponseStateAlert.Failure(it) }
                .collect() {
                    _oneCall.value = ResponseStateAlert.Success(it)
                }
        }
    }

    fun delete(it: AlertModel) {
        viewModelScope.launch(){
       repository.deleteAlertItem(it)
        getItems()
        }
    }
}