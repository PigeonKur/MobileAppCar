package com.example.myapplication.screen.Home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val supabase: SupabaseClient) : ViewModel() {
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    init {
        loadCars()
    }

    private fun loadCars() {
        viewModelScope.launch {
            try {
                val files = supabase.storage.from("cars").list()
                val carList = files.map { file ->
                    Car(
                        imageUrl = supabase.storage.from("cars").publicUrl(file.name),
                        name = "Автомобиль ${file.name.substringBeforeLast('.')}",
                        price = "Цена ${(1000..5000).random()}"
                    )
                }
                _cars.value = carList
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка при загрузке: ${e.message}")
            }
        }
    }
    fun rentCar(car: Car) {
        viewModelScope.launch {
            try {
                // Здесь логика аренды автомобиля
                println("Аренда автомобиля: ${car.name}")
            } catch (e: Exception) {
                println("Ошибка при аренде: ${e.message}")
            }
        }
    }
}

data class Car(
    val imageUrl: String,
    val name: String,
    val price: String
)



