package com.example.myapplication.Methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.screen.Home.CarViewModel
import io.github.jan.supabase.SupabaseClient

class CarViewModelFactory(
    private val supabase: SupabaseClient
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            return CarViewModel(supabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
