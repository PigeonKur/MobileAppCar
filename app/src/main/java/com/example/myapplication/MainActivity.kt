package com.example.myapplication

import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screen.Home.HomeScreen
import com.example.myapplication.screen.Login.LoginScreen
import com.example.myapplication.screen.Profile.Profile
import com.example.myapplication.screen.PutForSale.PutForSale
import com.example.myapplication.screen.Register.RegisterScreen
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://mbtorrjppkvwjngctpya.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1idG9ycmpwcGt2d2puZ2N0cHlhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzk1MjkxNjIsImV4cCI6MjA1NTEwNTE2Mn0.tafaHh11Mz7BMkVvrP2MgZ5rlWHuTfY2kStDRTM2jUY"
){
    install(Postgrest)
    install(Auth)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator()
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("putForSale") { PutForSale(navController)  }
        composable("profile") { Profile(navController)  }
    }
}


