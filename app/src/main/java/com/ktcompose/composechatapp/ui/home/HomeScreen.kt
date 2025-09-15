package com.ktcompose.composechatapp.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ktcompose.composechatapp.constants.Colors
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.ui.call.CallsScreen
import com.ktcompose.composechatapp.ui.chat.ChatListScreen
import com.ktcompose.composechatapp.ui.chat.MessageScreen
import com.ktcompose.composechatapp.ui.chat.MessageViewModel
import com.ktcompose.composechatapp.ui.group.GroupsScreen
import com.ktcompose.composechatapp.ui.user.UsersScreen

@Composable
fun HomeScreen(rootNav : NavHostController) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Chats,
        BottomNavItem.Calls,
        BottomNavItem.Users,
        BottomNavItem.Groups
    )

    Scaffold(
        bottomBar = {
            // Observe the current route
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            // Only show bottom bar for bottomNavItems routes
            if (currentRoute in bottomNavItems.map { it.route }) {
                NavigationBar {
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = currentBackStackEntry?.destination

                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.route == item.route,
                            onClick = {
                                if (currentDestination?.route != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    println("General log: Already initialized")
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Chats.route,
            modifier = Modifier.padding(
                // Only apply padding for screens with bottom bar
                if (navController.currentBackStackEntryAsState().value?.destination?.route in bottomNavItems.map { it.route })
                    innerPadding
                else PaddingValues(0.dp)
            )
        ) {
            composable(BottomNavItem.Chats.route) {
                ChatListScreen(navController = navController, rootNav)
            }
            composable(BottomNavItem.Calls.route) { CallsScreen() }
            composable(BottomNavItem.Users.route) { UsersScreen() }
            composable(BottomNavItem.Groups.route) { GroupsScreen() }
            composable(
                route = "${K.MESSAGE_SCREEN}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    rootNav.getBackStackEntry(K.HOME_SCREEN) // remember Home as parent to prevent reload
                }
                val messageViewModel: MessageViewModel = hiltViewModel(parentEntry)

                val userId = backStackEntry.arguments?.getString("userId")

                // collect the list from ViewModel (Room + Firebase backed)
                val userRecords by messageViewModel.userRecords.collectAsState()

                val recordModel = userRecords.find { it.uid == userId }

                if (recordModel != null) {
                    MessageScreen(
                        userRecordModel = recordModel,
                        navController = navController,
                        messageViewModel = messageViewModel
                    )
                } else {
                    Text("Chat not found", color = Colors.defaultBlackWhite())
                }
            }
        }
    }
}