package com.ktcompose.composechatapp.utility

import com.ktcompose.composechatapp.data.model.UserRecordModel
import com.ktcompose.composechatapp.data.room.entity.UserRecordEntity

object Mappers {

// Mappers.kt

    fun UserRecordModel.toEntity(): UserRecordEntity =
        UserRecordEntity(
            uid = uid!!,
            displayName = displayName,
            username = username,
            profileUrl = profileUrl,
            lastMessage = lastMessage,
            timestamp = timestamp
        )

    fun UserRecordEntity.toModel(): UserRecordModel =
        UserRecordModel(
            uid = uid,
            displayName = displayName,
            username = username,
            profileUrl = profileUrl,
            lastMessage = lastMessage,
            timestamp = timestamp
        )

}