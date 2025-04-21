package com.example.myapplication.screen.Home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Models.Car
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.atomicfu.TraceBase.None.append
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
        Log.d("CarViewModel", "Загружаем автомобили...")
        viewModelScope.launch {
            try {
                val carList = supabase.postgrest["car_data"]
                    .select()
                    .decodeList<Car>()
                _cars.value = carList
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка при загрузке машин: ${e.message}")
            }
        }
    }

    fun rentCar(car: Car) {
        viewModelScope.launch {
            try {
                // Логика аренды
                println("Аренда автомобиля: ${car.name}")
            } catch (e: Exception) {
                println("Ошибка при аренде: ${e.message}")
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                supabase.storage.from("cars").delete(car.id)
                loadCars() // Перезагружаем список после удаления
            } catch (e: Exception) {
                println("Ошибка при удалении: ${e.message}")
            }
        }
    }
    fun updateCar(carId: String, newName: String, newPrice: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("CarViewModel", "Начинаем обновление машины $carId")

                val result = supabase.from("car_data")
                    .update({
                        set("Name", newName)
                        set("Price", newPrice)
                    }) {
                        filter {
                            eq("id", carId)
                        }
                    }

                Log.d("CarViewModel", "Результат обновления: $result")

                loadCars()
                onSuccess()

            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка обновления", e)

            }
        }
    }

}




