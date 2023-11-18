package com.example.nego.ui.chatbot

import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.chatbotResponse
import com.example.nego.Responses.chatbotmodel

class ChatbotViewModel : ViewModel() {

    private val userRepository = UserRepository()
     lateinit var chatbotList: ArrayList<chatbotmodel>


    fun callApi(model:String , promt:String ,max_tokens:Int,temperature:Int): LiveData<chatbotResponse?> {
        return userRepository.chatApicall(model , promt ,max_tokens,temperature)
    }

    fun addResponse(value:chatbotmodel){

        chatbotList.add(value);
    }

    fun getchatResponse(): ArrayList<chatbotmodel> {
        return chatbotList;
    }


}