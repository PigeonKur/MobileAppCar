package com.example.myapplication.screen

import RegisterViewModel
import ResultState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = viewModel()
    val state by viewModel.uiState
    val result by viewModel.resultState.collectAsState()

    LaunchedEffect(result) {
        println("Текущее состояние регистрации: $result")  // Отладка

        if (result is ResultState.Success) {
            delay(2000)
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.name,
            onValueChange = { newName ->
                viewModel.updateState(state.copy(name = newName))
            },
            label = { Text("Ваше имя") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.email,
            onValueChange = { newEmail ->
                viewModel.updateState(state.copy(email = newEmail))
            },
            label = { Text("Email") },
            isError = !state.isEmailValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        if (!state.isEmailValid && state.email.isNotEmpty()) {
            Text("Некорректный email", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.password,
            onValueChange = { newPassword ->
                viewModel.updateState(state.copy(password = newPassword))
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.confirmPassword,
            onValueChange = { newConfirmPassword ->
                viewModel.updateState(state.copy(confirmPassword = newConfirmPassword))
            },
            label = { Text("Подтвердите пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        if (state.password.isNotEmpty() &&
            state.confirmPassword.isNotEmpty() &&
            state.password != state.confirmPassword
        ) {
            Text("Пароли не совпадают", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.dateBirth,
            onValueChange = { newDateBirth ->
                viewModel.updateState(state.copy(
                    dateBirth = formatDateInput(newDateBirth)
                ))
            },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            placeholder = { Text("ДД.ММ.ГГГГ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (result) {
            is ResultState.Error -> {
                Text((result as ResultState.Error).message, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }
            is ResultState.Success -> {
                Text(
                    "Регистрация успешна! Идёт перенаправление...",
                    color = Color.Green
                )
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator()
            }
            ResultState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.register() // Убрали колбэк отсюда
            },
            enabled = result !is ResultState.Loading && result !is ResultState.Success,
            modifier = Modifier.fillMaxWidth()
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
