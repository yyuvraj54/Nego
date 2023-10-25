package com.example.nego.Repository

import androidx.lifecycle.MutableLiveData
import com.example.nego.Responses.LoginResponse
import com.example.nego.Retrofit.ApiService
import com.example.nego.Retrofit.RetrofitClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService: ApiService = RetrofitClient.apiService
    fun login(email: String, password: String): MutableLiveData<LoginResponse?> {
        val loginResponseData = MutableLiveData<LoginResponse?>()
        val requestObject = JsonObject()
        requestObject.addProperty("email", email)
        requestObject.addProperty("password",password)
        val call = apiService.login(requestObject)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponseData.value = loginResponse
                } else {
                    // Handle unsuccessful response
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle failure
            }
        })
        return loginResponseData
    }
}