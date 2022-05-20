package com.example.vrg_soft_test_task.domain.usecase

class LoadTopPublicationUseCase(private val topPublicationRepository: TopPublicationRepository) {
    suspend fun loadTopPublication() = topPublicationRepository.getTopPublicationFromApi()
}