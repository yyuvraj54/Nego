package com.example.nego.Auth

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.nego.R
import com.example.nego.Repository.UserRepository
import com.example.nego.SharedPrefsUtil
import de.hdodenhof.circleimageview.CircleImageView


class LoginViewModel (application: Application) : AndroidViewModel(application) {
    private val sharedPrefsUtil = SharedPrefsUtil(application)
    private val userRepository = UserRepository()
    private val PICK_IMAGE_REQUEST = 1
    private var  iconUrl = "https://firebasestorage.googleapis.com/v0/b/nego-a7774.appspot.com/o/default%2FprofileImage%2Favtar.jpg?alt=media&token=22a497a9-1927-4aab-a69d-06ed8e94a34b"



    fun saveLoginData(
        username: String,
        name: String,
        icon: String,
        id: String,
        password: String,
        phone: String,
        upiId: String
    ) {
        sharedPrefsUtil.saveUserInfo(username, name,icon,id,password,phone,upiId)
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

    fun firebasesignup(
        name: String,
        email: String,
        password: String,
        icon: String,
        phone: String,
        upiId: String
    ): LiveData<String> {
        return userRepository.firebaseSignup(name,email, password, icon,phone,upiId);
    }

    fun firebaselogin(email: String, password: String): LiveData<Pair<UserRepository.FirebaseUserResult, String>> {
        return userRepository.firebaseLogin(email, password);
    }



    fun selectImageFromGallery(activity: Activity,icon: CircleImageView) {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)



        }
    fun getProfileImage(): String {

        return iconUrl;
    }

    fun getDefaultProifileUri():Uri {
        val drawableResId = R.drawable.avtar
        val packageName="com.example.nego.Auth"
        val uriString = "android.resource://${packageName}/${drawableResId}"
        val uri: Uri = Uri.parse(uriString)
        return uri;
    }

}


