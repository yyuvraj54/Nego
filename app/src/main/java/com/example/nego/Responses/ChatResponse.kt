package com.example.nego.Responses

sealed class ChatResponse{
    data class Chat(var senderId: String?="", var receiverId: String?="",var message: String?="",var date:String?="",var type:String?="none")
    data class ChatPayment(var senderId: String?="", var receiverId: String?="",var message: String?="",var date:String?="", var amount: String?="null",var type:String?="none")
}


