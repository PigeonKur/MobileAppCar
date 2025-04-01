package com.example.myapplication.screen

import android.util.Log
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
import androidx.compose.runtime.getValue
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
import com.example.myapplication.Models.UserManager
import com.example.myapplication.R
import com.example.myapplication.supabase
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest

@Composable
fun Profile(navController: NavController) {
    val currentUser by UserManager.currentUser
    val isLoading = remember { mutableStateOf(currentUser == null) }

    val authUser = supabase.auth.currentUserOrNull()

    LaunchedEffect(authUser) {
        if (authUser != null) {
            try {
                Log.d("Profile", "Текущий аутентифицированный пользователь: ${authUser.email}")

                if (currentUser == null) {
                    val person = getPersonDataById(authUser.id)
                    person?.let { UserManager.setUser(it) }  // Исправлено здесь
                }
            } catch (e: Exception) {
                Log.e("Profile", "Ошибка получения данных", e)
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
            currentUser != null -> {
                Text(text = currentUser!!.name, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Дата рождения: ${currentUser!!.date_birth}",
                    color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${currentUser!!.email}",
                    color = Color.Gray, fontSize = 14.sp)
            }
            else -> {
                Text(text = "Данные не найдены", fontSize = 18.sp, color = Color.Red)
            }
        }
    }
}

suspend fun getPersonDataById(userId: String): Person? {
    Log.d("Profile", "Поиск пользователя по ID: $userId")
    return try {
        supabase
            .from("person")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<Person>()
            .also { person ->  // Исправлено здесь (заменили it на person)
                Log.d("Profile", "Результат поиска: ${person?.toString() ?: "null"}")
            }
    } catch (e: Exception) {
        Log.e("Profile", "Ошибка при поиске пользователя", e)
        null
    }
}




