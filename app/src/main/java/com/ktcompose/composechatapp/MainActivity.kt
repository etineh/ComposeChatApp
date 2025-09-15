package com.ktcompose.composechatapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ktcompose.composechatapp.constants.Colors
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.ui.auth.LoginScreen
import com.ktcompose.composechatapp.ui.auth.RegisterScreen
import com.ktcompose.composechatapp.ui.home.HomeScreen
import com.ktcompose.composechatapp.ui.theme.ComposeChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChatAppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Colors.background()
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = if (K.getCurrentUserId() == null) K.LOGIN_SCREEN else K.HOME_SCREEN
                    ) {

                        composable(K.LOGIN_SCREEN) {
                            LoginScreen(navController = navController)
                        }
                        composable(K.REGISTER_SCREEN) {
                            RegisterScreen(navController = navController)
                        }
                        composable(K.HOME_SCREEN) {
                            HomeScreen(rootNav = navController)
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, navigator: NavHostController, modifier: Modifier = Modifier) {
    Text(
        text = "logout $name!",
        fontSize = 45.sp,
        color = Colors.defaultBlackWhite(),
        modifier = modifier.clickable {
            FirebaseAuth.getInstance().signOut()
            navigator.navigate(K.LOGIN_SCREEN)
//            context.startActivity(Intent(context, RegisterS::class.java))

//            navController.navigate("register_screen") {
//                popUpTo("main_screen") { inclusive = true } // optional: prevent back navigation
//            }
        }
    )

}
