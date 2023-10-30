package com.example.nego.Auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.LoginResponse
import com.example.nego.Responses.SignupSuccess
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.SharedPrefsUtil
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application) : AndroidViewModel(application) {
    private val sharedPrefsUtil = SharedPrefsUtil(application)
    private val userRepository = UserRepository()
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

    fun login(email: String, password: String): LiveData<LoginResponse?> {
        return userRepository.startLogin(email, password);
    }


    fun signup(name:String,email: String, password: String): LiveData<ResponseBody> {
        return userRepository.startSignup(name,email, password);
    }


}


