package com.example.nego.ui.chatbot

import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.chatbotmodel
import okhttp3.ResponseBody

class ChatbotViewModel : ViewModel() {

    private val userRepository = UserRepository()

    lateinit var chatbotList: ArrayList<chatbotmodel>


    fun callApi(text:String ): LiveData<String?> {
        return userRepository.chatApicall(text)
    }

    fun addResponse(value:chatbotmodel){

        chatbotList.add(value);
    }

    fun getchatResponse(): ArrayList<chatbotmodel> {
        return chatbotList;
    }


}