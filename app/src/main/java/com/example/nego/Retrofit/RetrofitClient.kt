package com.example.nego.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://negoauth.onrender.com/api/v1/").addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}