package com.example.nego.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nego.R
import com.example.nego.Responses.User
import com.example.nego.Responses.chatbotmodel
import com.example.nego.chatScreen
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ChatBotAdapter(private val context: Context?, private val chatbotList:ArrayList<chatbotmodel>):
    RecyclerView.Adapter<ChatBotAdapter.ViewHolder>() {


    private val MESSAGE_TYPE_LEFT=0
    private val MESSAGE_TYPE_RIGHT=1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBotAdapter.ViewHolder {

        if(viewType==MESSAGE_TYPE_RIGHT){

            val view =LayoutInflater.from(parent.context).inflate(R.layout.item_right_side,parent,false)
            return ViewHolder(view)

        }
        else{
            val view =LayoutInflater.from(parent.context).inflate(R.layout.item_left_side,parent,false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatbotList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.messageText.text=chatbotList[position].message.toString()


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView =view.findViewById(R.id.message)


    }

    override fun getItemViewType(position: Int): Int {


        if(chatbotList[position].sendby=="person"){
            return MESSAGE_TYPE_RIGHT
        }
        else{
            return MESSAGE_TYPE_LEFT
        }

    }

}