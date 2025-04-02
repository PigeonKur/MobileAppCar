package com.example.myapplication.Models

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val date_birth: String? = null
)

