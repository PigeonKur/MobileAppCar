package com.example.myapplication.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Methods.isEmailValid
import com.example.myapplication.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordTrust by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var datebirth by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var registrationError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Ваше имя") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = email.isEmailValid()
            },
            label = { Text("Email") },
            isError = !isEmailValid
        )

        if (!isEmailValid) {
            Text("Некорректный email", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = passwordTrust,
            onValueChange = { passwordTrust = it },
            label = { Text("Введите пароль еще раз") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (passwordTrust != password) {
            Text("Пароли не совпадают", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = datebirth, onValueChange = { datebirth = formatDateInput(it) }, label = { Text("Дата рождения") })

        Spacer(modifier = Modifier.height(16.dp))

        if (registrationError != null) {
            Text(registrationError!!, color = Color.Red)
        }

        Button(
            onClick = {
                if (name.isNotEmpty() && password.isNotEmpty() && email.isEmailValid() && password == passwordTrust) {
                    coroutineScope.launch {
                        try {
                            val auth = supabase.auth
                            val result = auth.signUpWith(Email) {
                                this.email = email
                                this.password = password
                                data = buildJsonObject {
                                    put("name", name)
                                    put("date_birth", datebirth)
                                }
                            }

                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }

                        } catch (e: Exception) {
                            registrationError = "Ошибка регистрации: ${e.message}"
                        }
                    }
                } else {
                    registrationError = "Заполните все поля корректно!"
                }
            }
        ) { Text("Зарегистрироваться") }

    }
}

fun formatDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }

    return when (digits.length) {
        8 -> "${digits.substring(4, 8)}-${digits.substring(2, 4)}-${digits.substring(0, 2)}"
        6 -> "${digits.substring(0, 4)}-${digits.substring(4, 6)}"
        4 -> digits
        else -> input
    }
}
