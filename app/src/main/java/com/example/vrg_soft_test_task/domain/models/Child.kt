package com.example.vrg_soft_test_task.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Child(
    val data: ChildData
)
