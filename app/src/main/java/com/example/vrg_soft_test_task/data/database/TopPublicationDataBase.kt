package com.example.vrg_soft_test_task.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vrg_soft_test_task.data.database.dao.TopPublicationsDao
import com.example.vrg_soft_test_task.data.database.entity.TopPublicationEntity

@Database(entities = [TopPublicationEntity::class], version = 1)
abstract class TopPublicationDataBase : RoomDatabase(){
    abstract fun getTopPublicationsDao(): TopPublicationsDao
}