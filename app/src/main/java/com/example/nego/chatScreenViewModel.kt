package com.example.nego

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nego.Repository.UserRepository
import com.example.nego.Responses.Chat
import com.example.nego.Responses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class chatScreenViewModel(application: Application) :AndroidViewModel(application) {
    private val userRepository = UserRepository()
    var reference: DatabaseReference? =null
    private val utilities = Utilities()

    fun formatDateTime(date: Date): String {
        return SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault()).format(date)
    }
    fun sendMessage(senderId:String,receiverId:String,message:String,type:String="message"){
        val currentDate = Date()
        val formattedDateTime:String = formatDateTime(currentDate)


        reference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId);
        hashMap.put("receiverId",receiverId);
        hashMap.put("type",type);
        hashMap.put("message",message);
        hashMap.put("date",formattedDateTime);

        reference!!.child("Chat").push().setValue(hashMap)

//        var hashMapdate:HashMap<String,String> = HashMap()
//        hashMap.put("date",formattedDateTime);
//        reference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")
//        reference!!.child(receiverId).child("Dates").child(senderId).push().setValue(hashMapdate)

    }
    fun sendPaymentMessage(senderId:String,receiverId:String,message:String,amount:String,paymentState:String="-1" , type:String="paymentRequest", phone:String="null", upiId:String="null",username:String="none"){
        val currentDate = Date()
        val formattedDateTime:String = formatDateTime(currentDate)
        reference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId);
        hashMap.put("receiverId",receiverId);
        hashMap.put("type",type);
        hashMap.put("message",message);
        hashMap.put("amount",amount);
        hashMap.put("upiId",upiId);
        hashMap.put("username",username);
        hashMap.put("phone",phone);
        hashMap.put("status",paymentState);
        hashMap.put("date",formattedDateTime);

        reference!!.child("Chat").push().setValue(hashMap)
    }
    fun readMessages(senderId:String,receiverId: String): LiveData<ArrayList<Chat>>{
      return userRepository.readMessage(senderId,receiverId)
    }
    fun updateState() {
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(utilities.getFirebase().uid)
        val updates = hashMapOf<String, Any>("state" to  "online")
        databaseReference!!.updateChildren(updates)

    }


//    fun filterChats(query: String,_chatList:ArrayList<User>) {
//        val filteredList = _chatList.value?.filter { chat ->
//            chat.name.contains(query, true)
//        }
//        _filteredChatList.value = filteredList
//    }

}