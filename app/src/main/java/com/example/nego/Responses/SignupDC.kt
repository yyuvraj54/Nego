package com.example.nego.Responses


sealed class SignupDC<out T> {
    data class Success<T>(val data: T) : SignupDC<T>()
    data class Error(val message: String) : SignupDC<Nothing>()
}
data class SignupSuccess(
    val success: Boolean,
    val message: String
)

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

