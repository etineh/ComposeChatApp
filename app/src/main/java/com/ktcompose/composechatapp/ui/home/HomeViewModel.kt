package com.ktcompose.composechatapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _screenStack = MutableStateFlow<List<String>>(listOf(BottomNavItem.Chats.route))
    val screenStack: StateFlow<List<String>> = _screenStack.asStateFlow()

    private val _currentRoute = MutableStateFlow(BottomNavItem.Chats.route)
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    fun onNavItemSelected(route: String) {
        viewModelScope.launch {
            val currentStack = _screenStack.value.toMutableList()
            if (route !in currentStack) {
                currentStack.add(route)
                _screenStack.value = currentStack
            }
            _currentRoute.value = route // Bring to front by updating current route
        }
    }
}