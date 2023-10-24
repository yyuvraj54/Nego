package com.example.nego.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitClient {
    private const val BASE_URL = "https://your_base_url.com/" // Replace with your base URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitInstance(): Retrofit {
        return retrofit
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}