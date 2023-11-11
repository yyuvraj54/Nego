package com.example.nego.ui.profire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.User

class ProfileViewModel : ViewModel() {

    private val userRepository= UserRepository();


    fun getData():LiveData<ArrayList<User>>{
        return userRepository.getProfileData()
    }


}