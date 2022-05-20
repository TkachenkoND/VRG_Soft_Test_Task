package com.example.vrg_soft_test_task.di

import com.example.vrg_soft_test_task.data.repository_impl.TopPublicationRepositoryImpl
import com.example.vrg_soft_test_task.domain.repository.TopPublicationRepository
import org.koin.dsl.module

val dataModule = module {
    single<TopPublicationRepository> { TopPublicationRepositoryImpl(get()) }
}