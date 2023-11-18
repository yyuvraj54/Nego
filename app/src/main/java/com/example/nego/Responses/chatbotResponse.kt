package com.example.nego.Responses

data class chatbotResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val system_fingerprint: String,
    val usage: Usage
)