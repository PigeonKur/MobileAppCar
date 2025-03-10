package com.example.myapplication.screen

import androidx.navigation.NavController
import com.example.myapplication.Methods.isEmailValid
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginModel {
    fun handleLogin(
        email: String,
        password: String,
        navController: NavController,
        coroutineScope: CoroutineScope,
        auth: Auth,
        onError: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        if (email.isEmailValid() && password.isNotEmpty()) {
            onLoading(true)
            coroutineScope.launch {
                try {
                    auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } catch (e: Exception) {
                    onError("Ошибка входа: ${e.message}")
                } finally {
                    onLoading(false)
                }
            }
        } else {
            onError("Введите корректные данные")
        }
    }
}