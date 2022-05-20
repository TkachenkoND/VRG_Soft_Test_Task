package com.example.vrg_soft_test_task.data.repository_impl

import com.example.vrg_soft_test_task.data.services.TopPublicationServices
import com.example.vrg_soft_test_task.domain.repository.TopPublicationRepository

class TopPublicationRepositoryImpl (
    private val topPublicationServices: TopPublicationServices
) : TopPublicationRepository {
    override suspend fun getTopPublicationFromApi() = topPublicationServices.getTopPublication()
}