package com.example.myapplication.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Models.Person
import com.example.myapplication.R
import com.example.myapplication.supabase
import io.github.jan.supabase.gotrue.auth

@Composable
fun Profile(navController: NavController) {
    val currentPerson = remember { mutableStateOf<Person?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    val user = supabase.auth.currentUserOrNull()
    val userMetadata = user?.userMetadata ?: emptyMap()

    LaunchedEffect(userMetadata) {
        if (userMetadata != null) {
            try {
                val userId = java.util.UUID.randomUUID()
                val personData = Person(
                    id = userId.toString(),
                    name = userMetadata["name"] as? String ?: "Неизвестно",
                    email = userMetadata["email"] as? String ?: "Не указан",
                    date_birth = userMetadata["date_birth"] as? String ?: "Не указана",
                    password = userMetadata["password"] as? String ?: "Не указан"
                )

                currentPerson.value = personData
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        } else {
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Аватар профиля",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading.value -> {
                Text(text = "Загрузка...", fontSize = 18.sp, color = Color.Gray)
            }
            currentPerson.value != null -> {
                val user = currentPerson.value!!
                Text(text = user.name, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Дата рождения", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email", color = Color.Gray, fontSize = 14.sp)
                Text(text = user.email, fontSize = 18.sp, color = Color.Black)
            }
            else -> {
                Text(text = "Данные не найдены", fontSize = 18.sp, color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("home") }) {
            Text(text = "Сохранить")
        }
    }
}





