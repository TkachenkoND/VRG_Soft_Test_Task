package com.example.vrg_soft_test_task.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vrg_soft_test_task.data.database.entity.TopPublicationEntity

interface TopPublicationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopPublicationInDataBase(favoriteMovie: TopPublicationEntity)

    @Query("SELECT * from top_publications")
    fun getAllTopPublicationFromDataBase(): LiveData<List<TopPublicationEntity>>
}