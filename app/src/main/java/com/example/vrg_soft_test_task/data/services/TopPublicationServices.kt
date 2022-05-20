package com.example.vrg_soft_test_task.data.services

import com.example.vrg_soft_test_task.domain.models.TopPublicationModel
import retrofit2.http.GET

interface TopPublicationServices {
    @GET("/json")
    suspend fun getTopPublication(): TopPublicationModel
}