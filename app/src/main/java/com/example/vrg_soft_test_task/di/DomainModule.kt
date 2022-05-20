package com.example.vrg_soft_test_task.di

import com.example.vrg_soft_test_task.domain.usecase.LoadTopPublicationUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoadTopPublicationUseCase(get()) }
}