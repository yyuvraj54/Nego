package com.example.nego.Retrofit

import com.example.nego.Responses.LoginResponse
import com.example.nego.Responses.SignupSuccess
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body loginUser: loginUser?): Call<LoginResponse?>?

    @POST("signup")
    fun signup(@Body signupUser: signupUser?): Call<SignupSuccess?>?
}