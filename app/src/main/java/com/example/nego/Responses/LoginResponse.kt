package com.example.nego.Responses

import com.google.gson.annotations.SerializedName

sealed class LoginResponse<out T> {
    data class Success<T>(val data: T) : LoginResponse<T>()
    data class Error(val message: String) : LoginResponse<Nothing>()
    data class LoginResponseSuccess(
        @SerializedName("success")
        val success: String,

        @SerializedName("token")
        val token: String,

        @SerializedName("existingUser")
        val existingUser: ExistingUser,

        @SerializedName("message")
        val message: String
    )

    data class ExistingUser(
        @SerializedName("_id")
        val id: String,

        @SerializedName("name")
        val name: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("__v")
        val v: Int,

        @SerializedName("token")
        val userToken: String
    )

    data class LoginFailed(
        val Success: String,
        val message: String,
    )
}
