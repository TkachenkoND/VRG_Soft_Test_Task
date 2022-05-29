package com.example.vrg_soft_test_task.data.helper

import com.example.vrg_soft_test_task.data.database.entity.TopPublicationEntity
import com.example.vrg_soft_test_task.domain.models.Child
import com.example.vrg_soft_test_task.domain.models.ChildData
import com.example.vrg_soft_test_task.domain.models.DataTopPublication
import com.example.vrg_soft_test_task.domain.models.TopPublicationModel

fun toTopPublicationModel(listTopPublicationEntity: List<TopPublicationEntity>): TopPublicationModel {
    val listChild = mutableListOf<Child>()

    listTopPublicationEntity.forEach {
        listChild.add(Child(it.toChildData()))
    }

    return TopPublicationModel(
        data = DataTopPublication(
            children = listChild
        )
    )
}

fun TopPublicationEntity.toChildData() = ChildData(
    authorFullName = authorFullName,
    title = title,
    thumbnail = thumbnail,
    numComments = numComments,
    url = url,
    createdUTC = createdUTC
)

fun ChildData.toTopPublicationEntity() = TopPublicationEntity(
    authorFullName = authorFullName,
    title = title,
    thumbnail = thumbnail,
    numComments = numComments,
    url = url,
    createdUTC = createdUTC
)


