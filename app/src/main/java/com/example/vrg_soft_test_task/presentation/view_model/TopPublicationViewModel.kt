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
import com.example.vrg_soft_test_task.domain.usecase.SaveImageInStorageUseCase
import kotlinx.coroutines.launch

class TopPublicationViewModel(
    private val loadTopPublicationUseCase: LoadTopPublicationUseCase,
    private val saveImageInStorageUseCase: SaveImageInStorageUseCase,
    private val topPublicationsDao: TopPublicationsDao
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _topPublication = MutableLiveData<TopPublicationModel>()
    val topPublication: LiveData<TopPublicationModel> = _topPublication

    private val _isGetTopPublicationFromDb = MutableLiveData<Boolean>()
    val isGetTopPublicationFromDb: LiveData<Boolean> = _isGetTopPublicationFromDb

    //API
    fun loadTopPublicationVm() {
        viewModelScope.launch {
            try {
                _topPublication.postValue(loadTopPublicationUseCase.execute())

                _isLoading.postValue(true)
                Log.d("loadTopPublicationVm", "good")

            } catch (e: Exception) {
                Log.d("loadTopPublicationVm", e.toString())
                _isLoading.postValue(false)
            }
        }
    }

    //DataBase
    fun getTopPublicationFromDbVm() {
        viewModelScope.launch {
            try {
                val data =
                    toTopPublicationModel(topPublicationsDao.getAllTopPublicationFromDataBase())
                _topPublication.postValue(data)
                _isGetTopPublicationFromDb.postValue(true)
                Log.d("GetFromDbException", "good")
            } catch (e: Exception) {
                _isGetTopPublicationFromDb.postValue(false)
                Log.d("GetFromDbException", e.toString())
            }
        }
    }

    private suspend fun insertTopPublicationInDbVm(topPublicationEntity: List<TopPublicationEntity>) {
        try {
            topPublicationsDao.insertTopPublicationInDataBase(topPublicationEntity)
            Log.d("InsertInDb", "good")
        } catch (e: Exception) {
            Log.d("InsertInDbException", e.toString())
        }

    }

    private suspend fun updateTopPublicationInDbVm(topPublicationEntity: List<TopPublicationEntity>) {
        try {
            topPublicationsDao.updateTopPublicationInDataBase(topPublicationEntity)
            Log.d("UpdateInDb", "good")
        } catch (e: Exception) {
            Log.d("UpdateInDbException", e.toString())
        }
    }

    fun checkIsEmptyDbVm() {
        val listPublication = mutableListOf<TopPublicationEntity>()

        _topPublication.value?.data?.children?.forEach {
            listPublication.add(it.data.toTopPublicationEntity())
        }

        viewModelScope.launch {
            if (topPublicationsDao.checkIsEmptyDataBase() == null)
                insertTopPublicationInDbVm(listPublication)
            else
                updateTopPublicationInDbVm(listPublication)
        }
    }

    //Storage
    fun saveImageInStorageVm(imgUrl: String) {
        saveImageInStorageUseCase.execute(imgUrl)
    }

}