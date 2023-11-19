package com.example.nego.Retrofit

import com.example.nego.Responses.ApiRequestPalm
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body loginUser: loginUser?): Call<ResponseBody?>?

    @POST("signup")
    fun signup(@Body signupUser: signupUser?): Call<ResponseBody?>?

    @POST("models/text-bison-001:generateText?key=AIzaSyD91lqa7knpQ1kmyRBBfKNGPdvWnjHkbd0")
    @Headers("Content-Type: application/json")
    fun ApiCall(@Body apichat: ApiRequestPalm): Call<ResponseBody?>?
}