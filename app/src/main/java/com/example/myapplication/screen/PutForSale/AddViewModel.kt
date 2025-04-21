package com.example.myapplication.screen.PutForSale

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class AddViewModel : ViewModel() {

    fun addCar(
        manufacturer: String,
        model: String,
        price: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 1. Загрузка изображения
                val imageUrl = imageUri?.let { uri ->
                    val fileName = "car_${System.currentTimeMillis()}.jpg"
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        ?: throw IllegalArgumentException("Не удалось прочитать изображение")

                    supabase.storage.from("cars").upload(fileName, bytes)

                    // Получаем публичный URL
                    supabase.storage.from("cars").publicUrl(fileName)
                }

                // 2. Добавление данных в таблицу с возвратом ID
                supabase.from("car_data").insert(
                    mapOf(
                        "Manufacturer" to manufacturer,
                        "Name" to model,
                        "Price" to price,
                        "Image_url" to imageUrl
                    )
                ) {
                    // Правильный способ указать возвращаемые колонки
                    select(columns = Columns.list("id"))
                }

                onSuccess()
            } catch (e: Exception) {
                Log.e("AddViewModel", "Ошибка добавления", e)
                onError(when (e) {
                    is IllegalArgumentException -> e.message ?: "Ошибка загрузки изображения"
                    else -> "Не удалось добавить автомобиль. Попробуйте позже"
                })
            }
        }
    }

    private suspend fun uploadImage(uri: Uri, context: Context): Result<String> = runCatching {
        val fileName = "car_${System.currentTimeMillis()}.jpg"
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("Не удалось прочитать файл")

        supabase.storage.from("cars").upload(
            path = fileName,
            data = bytes,
            upsert = true
        )

        "https://mbtorrjppkvwjngctpya.supabase.co/storage/v1/object/public/cars/$fileName"
    }
}
