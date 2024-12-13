package com.example.weatheravel.ui.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("destinations") // Ganti dengan endpoint API-mu
    fun getDestinations(): Call<List<Destination>>

    @GET("destinations/{id}") // Endpoint untuk detail destinasi berdasarkan ID
    fun getDestinationDetail(@Path("id") id: Int): Call<Destination>

    @GET("destinations/{city}")
    fun getDestinationsByCity(@Path("city") city: String): Call<List<Destination>>

}