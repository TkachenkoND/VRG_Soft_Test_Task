package com.example.vrg_soft_test_task.di

import androidx.room.Room
import com.example.vrg_soft_test_task.data.database.TopPublicationDataBase
import org.koin.dsl.module

private const val DB_NAME = "Publications.db"

val dbModule = module {

    single {
        Room.databaseBuilder(get(), TopPublicationDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { get<TopPublicationDataBase>().getTopPublicationsDao() }
}