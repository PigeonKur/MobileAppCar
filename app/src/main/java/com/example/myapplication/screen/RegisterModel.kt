package com.example.myapplication.screen

import androidx.navigation.NavController
import com.example.myapplication.Methods.isEmailValid
import com.example.myapplication.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject


class RegisterModel {
    fun handleRegistration(
        name: String,
        email: String,
        password: String,
        passwordTrust: String,
        datebirth: String,
        navController: NavController,
        coroutineScope: CoroutineScope,
        onError: (String) -> Unit
    ) {
        if (name.isNotEmpty() && password.isNotEmpty() && email.isEmailValid() && password == passwordTrust) {
            coroutineScope.launch {
                try {
                    val auth = supabase.auth
                    val result = auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                        data = buildJsonObject {
                            put("name", JsonPrimitive(name))
                            put("date_birth", JsonPrimitive(datebirth))
                        }
                    }

                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }

                } catch (e: Exception) {
                    onError("Ошибка регистрации: ${e.message}")
                }
            }
        } else {
            onError("Заполните все поля корректно!")
        }
    }

}