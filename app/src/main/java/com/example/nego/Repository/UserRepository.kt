package com.example.nego.Repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nego.Auth.signup
import com.example.nego.Responses.LoginResponse
import com.example.nego.Responses.SignupDC
import com.example.nego.Responses.SignupFailure
import com.example.nego.Responses.SignupSuccess
import com.example.nego.Retrofit.ApiService
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.Retrofit.loginUser
import com.example.nego.Retrofit.signupUser
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

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

                } else if (response.code().toString() == "400") {
                    Log.d(TAG, "login Status: Wrong ID PASSWORD")
                }



            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d(TAG, "login Status:" + t.localizedMessage);
            }


             })
        return loginResponse;
    }



    fun startSignup(name:String , email:String ,password:String) : LiveData<ResponseBody>{
        val usersignup = signupUser(name,email, password);
        val signupresponse=MutableLiveData<ResponseBody>();


        RetrofitClient.apiService.signup(usersignup)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Log.d("Signup Response", "${response.message()} ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // Process the response body here
                    if (responseBody != null) {
                        val responseString = responseBody.string()
                        val signupSuccess = Gson().fromJson(responseString, SignupSuccess::class.java)
                        // Handle the success response here
                        Log.d(TAG, "Signup Status: ${signupSuccess.message}")
                    }
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val errorString = errorBody.string()
                        val signupFailure = Gson().fromJson(errorString, SignupFailure::class.java)
                        // Handle the failure response here
                        Log.d(TAG, "Signup Status: ${signupFailure.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.localizedMessage}")
                // Handle network failures or other errors
            }
        })




        return signupresponse;

    }
}
