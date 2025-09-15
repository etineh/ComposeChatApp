package com.ktcompose.composechatapp.utility

import com.ktcompose.composechatapp.data.model.UserRecordModel

object CacheUtils {
    private var userRecord: List<UserRecordModel> = listOf() // cache from ROOM if empty

    fun saveUsersRecord(users: List<UserRecordModel>) {
        userRecord = users
    }

    fun getUsersRecord(): List<UserRecordModel> {
        return userRecord
    }

    fun clearAll() {
        userRecord = listOf()
    }
}
