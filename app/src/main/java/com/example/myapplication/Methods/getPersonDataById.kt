package com.example.myapplication.Methods

import android.util.Log
import com.example.myapplication.Models.Person
import com.example.myapplication.supabase
import io.github.jan.supabase.postgrest.from

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
            .also { person ->
                Log.d("Profile", "Результат поиска: ${person?.toString() ?: "null"}")
            }
    } catch (e: Exception) {
        Log.e("Profile", "Ошибка при поиске пользователя", e)
        null
    }
}