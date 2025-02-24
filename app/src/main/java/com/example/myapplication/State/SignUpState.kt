package com.example.supabasesimpleproject.Domain.State


data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val dateBirth: String = "",
)


