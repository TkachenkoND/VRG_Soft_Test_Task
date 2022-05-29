package com.example.vrg_soft_test_task.di

import com.example.vrg_soft_test_task.data.storage.SaveImageStorage
import org.koin.dsl.module

val storageModule = module {
    single { SaveImageStorage(get()) }
}