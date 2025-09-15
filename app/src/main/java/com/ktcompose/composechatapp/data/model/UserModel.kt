package com.ktcompose.composechatapp.data.model

data class UserModel(
    val uid: String? = null,
    val displayName: String? = null,
    val profileUrl: String? = null,
    val username: String? = null,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)