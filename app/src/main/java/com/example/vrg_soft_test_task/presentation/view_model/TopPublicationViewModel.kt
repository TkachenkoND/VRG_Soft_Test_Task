package com.example.vrg_soft_test_task.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vrg_soft_test_task.data.database.dao.TopPublicationsDao
import com.example.vrg_soft_test_task.data.database.entity.TopPublicationEntity
import com.example.vrg_soft_test_task.data.toTopPublicationEntity
import com.example.vrg_soft_test_task.data.toTopPublicationModel
import com.example.vrg_soft_test_task.domain.models.TopPublicationModel
import com.example.vrg_soft_test_task.domain.usecase.LoadTopPublicationUseCase
import kotlinx.coroutines.launch

class TopPublicationViewModel(
    private val loadTopPublicationUseCase: LoadTopPublicationUseCase,
    private val topPublicationsDao: TopPublicationsDao
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _topPublication = MutableLiveData<TopPublicationModel>()
    val topPublication: LiveData<TopPublicationModel> = _topPublication

    private val _isGetTopPublicationFromDb = MutableLiveData<Boolean>()
    val isGetTopPublicationFromDb: LiveData<Boolean> = _isGetTopPublicationFromDb

    private val _isInsertTopPublicationInDb = MutableLiveData<Boolean>()
    val isInsertTopPublicationInDb: LiveData<Boolean> = _isInsertTopPublicationInDb

    //API
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

    //DataBase
    fun getTopPublicationFromDbVm() {
        try {
            _topPublication.value =
                toTopPublicationModel(topPublicationsDao.getAllTopPublicationFromDataBase().value!!)
            _isGetTopPublicationFromDb.value = true
        } catch (e: Exception) {
            Log.d("GetFromDbException", e.toString())
            _isGetTopPublicationFromDb.value = false
        }
    }

    fun insertTopPublicationInDbVm(topPublicationModel: TopPublicationModel) {
        val listPublication = mutableListOf<TopPublicationEntity>()

        topPublicationModel.data.children.forEach {
            listPublication.add(it.data.toTopPublicationEntity())
        }

        viewModelScope.launch {
            try {
                listPublication.forEach {
                    topPublicationsDao.insertTopPublicationInDataBase(it)
                }
                _isInsertTopPublicationInDb.postValue(true)
            } catch (e: Exception) {
                Log.d("InsertInDbException", e.toString())
                _isInsertTopPublicationInDb.postValue(false)
            }
        }
    }
}