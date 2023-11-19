package com.example.nego.Responses

data class ApiRequestPalm(
    val prompt: Prompt
)

data class Prompt(
    val text: String
)