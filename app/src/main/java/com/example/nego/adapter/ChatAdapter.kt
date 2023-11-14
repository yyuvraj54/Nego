package com.example.nego.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nego.R
import com.example.nego.Responses.Chat
import com.example.nego.chatScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView


class ChatAdapter(private val context: Context,private val chatList:ArrayList<Chat>):
RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private val MESSAGE_TYPE_LEFT=0
    private val MESSAGE_TYPE_RIGHT=1
    var firebaseUser:FirebaseUser?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {

        if(viewType==MESSAGE_TYPE_RIGHT){

            val view =LayoutInflater.from(parent.context).inflate(R.layout.item_right_side,parent,false)
            return ViewHolder(view)

        }
        else{
            val view =LayoutInflater.from(parent.context).inflate(R.layout.item_left_side,parent,false)
            return ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {

        val chat =chatList[position]
        holder.textUsername.text=chat.message

//        if (context != null) {
//            Glide.with(context)
//                .load(chat.)
//                .placeholder(R.drawable.avtar) // Optional: Placeholder image while loading
//                .error(R.drawable.avtar) // Optional: Image to display if loading fails
//                .into(holder.imgUser)
//        }
        holder.datetext.text=chat.date

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textUsername: TextView =view.findViewById(R.id.message)
        val datetext: TextView =view.findViewById(R.id.date)


    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if(chatList[position].senderId==firebaseUser!!.uid){
            return MESSAGE_TYPE_RIGHT
        }
        else{
            return MESSAGE_TYPE_LEFT
        }

    }

}