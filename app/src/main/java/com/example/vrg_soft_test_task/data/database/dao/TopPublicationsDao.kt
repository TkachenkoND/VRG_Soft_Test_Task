package com.example.vrg_soft_test_task.data.database.dao

import androidx.room.*
import com.example.vrg_soft_test_task.data.database.entity.TopPublicationEntity

@Dao
interface TopPublicationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopPublicationInDataBase(publication: List<TopPublicationEntity>)

    @Query("SELECT * from top_publications")
    suspend fun getAllTopPublicationFromDataBase(): List<TopPublicationEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTopPublicationInDataBase(listPublication: List<TopPublicationEntity>)

    @Query("SELECT * from top_publications LIMIT 1")
    suspend fun checkIsEmptyDataBase(): TopPublicationEntity
}