package com.example.myapplication.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Car(
        val id: String,
        @SerialName("Manufacturer") val Manufacturer: String,
        @SerialName("Name") val name: String,
        @SerialName("Price") val price: String,
        @SerialName("Image_url") val imageUrl: String
)

