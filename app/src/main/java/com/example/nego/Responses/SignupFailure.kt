package com.example.nego.Responses

data class SignupFailure(
    val success: Boolean,
    val message: String,
    val code: List<Code>
)

data class Code(
    val _id: String,
    val name: String,
    val email: String,
    val password: String,
    val __v: Int
)
