package com.example.weatheravel.ui.API

data class Destination(
    val id: Int,
    val name: String,
    val description: String,
    val weather: String,
    val imageUrl: String, // URL gambar destinasi
    val category: String, // Kategori destinasi
    val province: String, // Provinsi destinasi
    val rating: Double // Rating destinasi
)
