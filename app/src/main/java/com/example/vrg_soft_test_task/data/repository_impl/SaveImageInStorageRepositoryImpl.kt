package com.example.vrg_soft_test_task.data.repository_impl

import com.example.vrg_soft_test_task.data.storage.SaveImageStorage
import com.example.vrg_soft_test_task.domain.repository.SaveImageInStorageRepository

class SaveImageInStorageRepositoryImpl(
    private val saveImageStorage: SaveImageStorage
) : SaveImageInStorageRepository {

    override fun saveImageInStorage(imgUrl: String) {
        saveImageStorage.saveImage(imgUrl)
    }
}