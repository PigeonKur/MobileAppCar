package com.example.myapplication.screen.Home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Models.Car
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val supabase: SupabaseClient) : ViewModel() {
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()
    var selectedSortOption by mutableStateOf("price_asc")
    var selectedManufacturer by mutableStateOf<String?>(null)

    init {
        loadCars()
    }

    fun loadCars() {
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

    fun deleteCar(car: Car, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                // 1. Логирование перед удалением
                Log.d("CarViewModel", "Начинаем удаление машины ID: ${car.id}")
                Log.d("CarViewModel", "Image URL: ${car.imageUrl}")

                // 2. Удаляем запись из таблицы car_data
                supabase.from("car_data")
                    .delete {
                        filter {
                            eq("id", car.id)
                        }
                    }
                Log.d("CarViewModel", "Запись из car_data удалена")

                // 3. Извлекаем путь к изображению
                val imagePath = car.imageUrl?.let { url ->
                    when {
                        url.contains("storage/v1/object/public/cars/") ->
                            url.substringAfter("storage/v1/object/public/cars/")
                        url.contains("storage/v1/object/cars/") ->
                            url.substringAfter("storage/v1/object/cars/")
                        else -> url.substringAfterLast("/")
                    }.also { path ->
                        Log.d("CarViewModel", "Извлеченный путь к файлу: $path")
                    }
                }

                // 4. Удаляем изображение из storage
                imagePath?.let { path ->
                    Log.d("CarViewModel", "Пытаемся удалить файл: $path")
                    try {
                        supabase.storage.from("cars").delete(path)
                        Log.d("CarViewModel", "Файл успешно удален из storage")
                    } catch (e: Exception) {
                        Log.e("CarViewModel", "Ошибка при удалении файла", e)
                        throw Exception("Ошибка удаления файла: ${e.message}")
                    }
                }

                // 5. Обновляем список
                loadCars()
                onSuccess()

            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("row-level security") == true ->
                        "Ошибка прав доступа. Проверьте RLS политики"
                    e.message?.contains("not found") == true ->
                        "Файл не найден в storage"
                    else -> "Ошибка при удалении: ${e.message ?: "неизвестная ошибка"}"
                }
                Log.e("CarViewModel", errorMsg, e)
                onError(errorMsg)
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
    fun searchCars(query: String) {
        viewModelScope.launch {
            try {
                // Просто получаем все машины
                val allCars = supabase.from("car_data")
                    .select()
                    .decodeList<Car>()

                // Фильтруем локально если есть запрос
                val result = if (query.isNotEmpty()) {
                    val lowerQuery = query.lowercase()
                    allCars.filter { car ->
                        car.Manufacturer?.lowercase()?.contains(lowerQuery) == true ||
                                car.name?.lowercase()?.contains(lowerQuery) == true
                    }
                } else {
                    allCars
                }

                _cars.value = result
                Log.d("CarViewModel", "Найдено машин: ${result.size}")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка загрузки", e)
            }
        }
    }

    fun getUniqueManufacturers(): List<String> {
        return cars.value
            .mapNotNull { it.Manufacturer }
            .distinct()
            .sorted()
    }

    // Сортировка автомобилей
    fun sortCars(ascending: Boolean) {
        _cars.value = if (ascending) {
            cars.value.sortedBy { it.price?.toIntOrNull() ?: 0 }
        } else {
            cars.value.sortedByDescending { it.price?.toIntOrNull() ?: 0 }
        }
    }

    // Фильтрация по производителю
    fun filterByManufacturer(manufacturer: String?) {
        viewModelScope.launch {
            if (manufacturer == null) {
                loadCars() // Сброс фильтра
            } else {
                val allCars = supabase.from("car_data")
                    .select()
                    .decodeList<Car>()
                _cars.value = allCars.filter { it.Manufacturer == manufacturer }
            }
        }
    }

    fun getManufacturers(): List<String> {
        return cars.value.mapNotNull { it.Manufacturer }.distinct()
    }

}




