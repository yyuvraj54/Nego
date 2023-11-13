package com.example.nego

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.Chat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class chatScreenViewModel(application: Application) :AndroidViewModel(application) {
    private val userRepository = UserRepository()
    var reference: DatabaseReference? =null

    fun sendMessage(senderId:String,receiverId:String,message:String){

        reference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId);
        hashMap.put("receiverId",receiverId);
        hashMap.put("message",message);

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessages(senderId:String,receiverId: String): LiveData<ArrayList<Chat>>{
      return userRepository.readMessage(senderId,receiverId)



    }


}