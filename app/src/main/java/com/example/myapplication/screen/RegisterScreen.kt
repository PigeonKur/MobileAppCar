package com.example.myapplication.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Methods.isEmailValid


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
    val registerModel = RegisterModel()

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
                registerModel.handleRegistration(
                    name = name,
                    email = email,
                    password = password,
                    passwordTrust = passwordTrust,
                    datebirth = datebirth,
                    navController = navController,
                    coroutineScope = coroutineScope,
                    onError = { registrationError = it }
                )
            }
        ) {
            Text("Зарегистрироваться")
        }


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
