package com.ktcompose.composechatapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ktcompose.composechatapp.utility.CacheUtils
import com.ktcompose.composechatapp.data.model.AuthUiState
import com.ktcompose.composechatapp.data.repository.AppRepository
import com.ktcompose.composechatapp.data.model.RegisterModel
import com.ktcompose.composechatapp.data.repository.LocalStorageRepo
import com.ktcompose.composechatapp.extensions.isEmailValidExt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository,
    private val localStorageRepo: LocalStorageRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    // Update form inputs
    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onDisplayNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(displayName = newName)
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
    }

    fun registerUser() {
        val state = _uiState.value
        if (!state.email.isEmailValidExt()) return
        val errorMessage = validateRegisterInputs(state)

        if (errorMessage != null) {
            _uiState.value = state.copy(error = errorMessage)
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            val registerModel = RegisterModel(
                displayName = state.displayName,
                username = state.username,
                email = state.email,
                password = state.password
            )

            val result = repository.registerUser(registerModel)

            _uiState.value = if (result.isSuccess) {
                state.copy(isLoading = false, isSuccess = true)
            } else {
                state.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    private fun validateRegisterInputs(state: AuthUiState): String? {
        return when {
            !state.email.isEmailValidExt() -> "Invalid email format"
            state.displayName.isBlank() -> "Display name cannot be empty"
            state.password.isBlank() -> "Password cannot be empty"
            state.username.length < 4 -> "Username must be greater than 4 characters"
            else -> null // no error
        }
    }

    // Login existing user
    fun loginUser() {
        val state = _uiState.value
        if (!state.email.isEmailValidExt()) {
            _uiState.value = state.copy(error = "Invalid email format")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = state.copy(error = "Password cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            val result = repository.loginUser(state.email.trim(), state.password.trim())

            _uiState.value = if (result.isSuccess) {
                state.copy(isLoading = false, isSuccess = true)
            } else {
                state.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        localStorageRepo.clearMyUser()
        CacheUtils.clearAll()
    }
}
