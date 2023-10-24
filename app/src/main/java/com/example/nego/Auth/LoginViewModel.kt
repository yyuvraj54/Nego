package com.example.nego.Auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nego.Responses.LoginResponse
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.SharedPrefsUtil
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application) : AndroidViewModel(application) {
    private val sharedPrefsUtil = SharedPrefsUtil(application)
    fun saveLoginData(username: String, password: String) {
        sharedPrefsUtil.saveUserInfo(username, password)
        sharedPrefsUtil.saveLoginStatus(true)
    }

    fun checkLoginStatus(): Boolean {
        return sharedPrefsUtil.getLoginStatus()
    }

    fun logout() {
        sharedPrefsUtil.clearLoginInfo()
    }

    fun login(email: String, password: String) {
        val apiService = RetrofitClient.apiService
        val requestObject = JsonObject()
        requestObject.addProperty("email", email)
        requestObject.addProperty("password", password)

        val call = apiService.login(requestObject)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

}


