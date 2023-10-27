package com.example.nego.Repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nego.Responses.LoginResponse
import com.example.nego.Retrofit.ApiService
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.Retrofit.loginUser
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    fun  startLogin(email: String, password: String): LiveData<LoginResponse?> {
        val userlogin = loginUser(email, password);
        val loginResponse=MutableLiveData<LoginResponse>();
        Log.d(TAG, "startLogin: "+userlogin.toString());
        Log.d(TAG, "startLogin: "+email+password);
        RetrofitClient.apiService.login(userlogin)?.enqueue(object :Callback<LoginResponse?>{
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                Log.d("LoginIn", response.message()+" "+response.code())
                if (response.code().toString() == "200") {
                    val gotResponse = response.body()
                    loginResponse.value=response.body();
                    Log.d("LoginIn", "${response.body().toString()}")
                    if (gotResponse != null) {


                        val status = gotResponse.success
                        val email = gotResponse.existingUser.email
                        val name = gotResponse.existingUser.name

                        if (status == "True") {

//                            Log.d(TAG, "status:" + status)
//                            Log.d(TAG, "email:" + email)
//                            Log.d(TAG, "name:" + name)
//                            Log.d(TAG, "login Status: Login Sucess")
                        } else {Log.d(TAG, "login Status: Wrong ID PASSWORD") }

                    }

                } else if (response.code().toString() == "401") {
                    Log.d(TAG, "login Status: Wrong ID PASSWORD")
                }



            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d(TAG, "login Status:" + t.localizedMessage);
            }


             })
        return loginResponse;
    }
}