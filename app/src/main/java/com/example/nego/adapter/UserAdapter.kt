package com.example.nego.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nego.R
import com.example.nego.Responses.User

class UserAdapter(private val context: Context?, private val userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user =userList[position]
        holder.textUserName.text=user.userName


    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textUserName:TextView =view.findViewById(R.id.person_name)
//        val imgUser:TextView =view.findViewById(R.id.profile_image)
        val lasttext:TextView =view.findViewById(R.id.last_message)
    }

}