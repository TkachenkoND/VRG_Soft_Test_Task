package com.example.vrg_soft_test_task.di

import com.example.vrg_soft_test_task.presentation.view_model.TopPublicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TopPublicationViewModel(get()) }
}