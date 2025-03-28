package com.example.myapplication.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Methods.isEmailValid
import com.example.myapplication.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val loginModel = LoginModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Text(text = "Некорректный email", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loginError != null) {
            Text(loginError!!, color = Color.Red)
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    loginModel.handleLogin(
                        email = email,
                        password = password,
                        navController = navController,
                        coroutineScope = CoroutineScope(Dispatchers.Main),
                        auth = supabase.auth,
                        onError = { loginError = it },
                        onLoading = { isLoading = it }
                    )
                },
                enabled = email.isEmailValid() && password.isNotEmpty()
            ) {
                Text("Войти")
            }
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Регистрация")
        }
    }
}



