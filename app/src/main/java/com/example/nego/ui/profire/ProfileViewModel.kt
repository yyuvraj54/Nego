package com.example.nego.ui.profire

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.User
import com.example.nego.SharedPrefsUtil

class ProfileViewModel: ViewModel() {

    private val userRepository= UserRepository();


    fun getData():LiveData<ArrayList<User>>{
        return userRepository.getProfileData()
    }



}