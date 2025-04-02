package com.example.myapplication.Methods

fun formatDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }

        return when (digits.length) {
            8 -> "${digits.substring(4, 8)}-${digits.substring(2, 4)}-${digits.substring(0, 2)}"
            6 -> "${digits.substring(0, 4)}-${digits.substring(4, 6)}"
            4 -> digits
            else -> input
        }
    }
