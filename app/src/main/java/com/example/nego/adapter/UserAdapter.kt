package com.example.nego.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nego.R
import com.example.nego.Responses.User
import com.example.nego.chatScreen
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.log

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

        try {
            val user = userList[position]
            holder.textUserName.text = user.userName

            if (context != null) {
                Glide.with(context)
                    .load(user.profileImage)
                    .placeholder(R.drawable.avtar) // Optional: Placeholder image while loading
                    .error(R.drawable.avtar) // Optional: Image to display if loading fails
                    .into(holder.imgUser)
            }
            holder.State.text = user.state
            if (user.state == "offline") {
                holder.State.setTextColor(ContextCompat.getColor(context!!, R.color.black)) // Change to your desired offline color
            } else {
                holder.State.setTextColor(ContextCompat.getColor(context!!, R.color.green))
            }




        holder.userItem.setOnClickListener {

            val intent= Intent(context,chatScreen::class.java)
            intent.putExtra("UserId",user.userId)
            intent.putExtra("phone",user.phone)
            context?.startActivity(intent)

        }
        }

        catch (e: Exception) {
            Log.d(TAG, "onBindViewHolder: "+e)
        }

    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textUserName:TextView =view.findViewById(R.id.person_name)
        val imgUser: CircleImageView =view.findViewById(R.id.profile_image)
        val lasttext:TextView =view.findViewById(R.id.last_message)
        val userItem: CardView =view.findViewById(R.id.UserItem)
        val State: TextView =view.findViewById(R.id.onlineState)

    }

}