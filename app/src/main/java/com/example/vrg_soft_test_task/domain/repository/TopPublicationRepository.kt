package com.example.vrg_soft_test_task.domain.repository

import com.example.vrg_soft_test_task.domain.models.TopPublicationModel

interface TopPublicationRepository {
    suspend fun getTopPublicationFromApi(): TopPublicationModel
}