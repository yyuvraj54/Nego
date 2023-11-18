package com.example.nego.Retrofit

data class ApiChat(

    val model: String,
    val prompt: String,
    val max_tokens: Int,
    val temperature: Int
)