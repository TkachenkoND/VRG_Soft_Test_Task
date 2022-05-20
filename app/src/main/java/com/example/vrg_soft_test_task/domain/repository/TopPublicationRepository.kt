package com.example.vrg_soft_test_task.domain.repository

interface TopPublicationRepository {
    suspend fun getTopPublicationFromApi(): TopPublicationModel
}