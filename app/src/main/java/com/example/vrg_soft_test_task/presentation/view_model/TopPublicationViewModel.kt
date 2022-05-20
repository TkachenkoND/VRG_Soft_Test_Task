package com.example.vrg_soft_test_task.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vrg_soft_test_task.domain.models.TopPublicationModel
import com.example.vrg_soft_test_task.domain.usecase.LoadTopPublicationUseCase
import kotlinx.coroutines.launch

class TopPublicationViewModel(
    private val loadTopPublicationUseCase: LoadTopPublicationUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _topPublication = MutableLiveData<TopPublicationModel>()
    val topPublication: LiveData<TopPublicationModel> = _topPublication

    fun loadTopPublicationVm() {
        viewModelScope.launch {
            try {
                _topPublication.postValue(loadTopPublicationUseCase.loadTopPublication())
                _isLoading.postValue(true)
            } catch (e: Exception) {
                Log.d("LoadException", e.toString())
                _isLoading.postValue(false)
            }
        }
    }
}