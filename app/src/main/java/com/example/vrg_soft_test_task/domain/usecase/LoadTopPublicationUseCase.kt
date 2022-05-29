package com.example.vrg_soft_test_task.domain.usecase

import com.example.vrg_soft_test_task.domain.repository.TopPublicationRepository

class LoadTopPublicationUseCase(private val topPublicationRepository: TopPublicationRepository) {
    suspend fun execute() = topPublicationRepository.getTopPublicationFromApi()
}