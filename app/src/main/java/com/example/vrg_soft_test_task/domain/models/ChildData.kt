package com.example.vrg_soft_test_task.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChildData(
    @Json(name = "author_fullname")
    val authorFullName: String,
    val title: String,
    val thumbnail: String,
    @Json(name = "num_comments")
    val numComments: Long,
    val url: String,
    @Json(name = "created_utc")
    val createdUTC: Double,
)
