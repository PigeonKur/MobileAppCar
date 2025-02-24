package com.example.myapplication.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val name: String,
    val email: String,
    val password: String,
    val date_birth: String
)
