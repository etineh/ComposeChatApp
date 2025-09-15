package com.ktcompose.composechatapp.data.model

data class RegisterModel(
    val displayName: String,
    val username: String,
    val profileUrl: String? = null,
    val email: String,
    val password: String,
)

data class AuthUiState(
    val displayName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    var error: String? = null,
    val isSuccess: Boolean = false
)