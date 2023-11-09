package com.example.nego.ui.chats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.User
import com.example.nego.adapter.UserAdapter

class ChatsViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _text = MutableLiveData<String>().apply { value = "This is gallery Fragment" }
    val text: LiveData<String> = _text



    fun getUsersListFromRepo():LiveData<ArrayList<User>>{
        return  userRepository.getUsersList()
    }



}