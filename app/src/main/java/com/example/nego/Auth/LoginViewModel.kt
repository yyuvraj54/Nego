package com.example.nego.Auth

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.LoginResponse
import com.example.nego.Responses.SignupSuccess
import com.example.nego.Retrofit.RetrofitClient
import com.example.nego.SharedPrefsUtil
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application) : AndroidViewModel(application) {
    private val sharedPrefsUtil = SharedPrefsUtil(application)
    private val userRepository = UserRepository()
    private val PICK_IMAGE_REQUEST = 1
    fun saveLoginData(username: String,name:String,icon:String,id:String, password: String) {
        sharedPrefsUtil.saveUserInfo(username, name,icon,id,password)
        sharedPrefsUtil.saveLoginStatus(true)
    }

    fun checkLoginStatus(): Boolean {
        return sharedPrefsUtil.getLoginStatus()
    }
//    fun saveLoginStatus(username: String,name:String,icon:String,id:String, password: String) {
//        return sharedPrefsUtil.saveUserInfo(username,name,icon,id, password)
//    }

    fun logout() {
        sharedPrefsUtil.clearLoginInfo()
    }

//    fun login(email: String, password: String): LiveData<ResponseBody?> {
//        return userRepository.startLogin(email, password);
//    }
//
//
//    fun signup(name:String,email: String, password: String): LiveData<ResponseBody> {
//        return userRepository.startSignup(name,email, password);
//    }

    fun firebasesignup(name:String,email: String, password: String): LiveData<String> {
        return userRepository.firebaseSignup(name,email, password);
    }

    fun firebaselogin(email: String, password: String): LiveData<Pair<UserRepository.FirebaseUserResult, String>> {
        return userRepository.firebaseLogin(email, password);
    }



    fun selectImageFromGallery(activity: Activity,icon: CircleImageView) {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }
    }

}


