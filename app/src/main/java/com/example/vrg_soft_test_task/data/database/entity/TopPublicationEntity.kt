package com.example.vrg_soft_test_task.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_publications")
data class TopPublicationEntity(
    @PrimaryKey(autoGenerate = true)
    var publicationId: Int? = null,

    @ColumnInfo(name = "author_full_name")
    val authorFullName: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,

    @ColumnInfo(name = "num_comments")
    val numComments: Long,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "created_UTC")
    val createdUTC: Double
)
