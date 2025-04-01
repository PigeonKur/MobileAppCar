package com.example.myapplication.Models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.Person
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val date_birth: String? = null
)

