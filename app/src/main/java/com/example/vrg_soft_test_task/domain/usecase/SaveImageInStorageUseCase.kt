package com.example.vrg_soft_test_task.domain.usecase

import com.example.vrg_soft_test_task.domain.repository.SaveImageInStorageRepository

class SaveImageInStorageUseCase(private val saveImageInStorageRepository: SaveImageInStorageRepository) {
    fun execute(imgUrl: String) = saveImageInStorageRepository.saveImageInStorage(imgUrl)
}