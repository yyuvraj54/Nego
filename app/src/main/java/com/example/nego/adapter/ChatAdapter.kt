package com.example.nego.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nego.R
import com.example.nego.Responses.Chat
import com.example.nego.Utilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val utilities = Utilities()
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    private val PAYMENT_VIEW_LEFT = 2
    private val PAYMENT_VIEW_RIGHT = 3
    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right_side, parent, false)
                MessageViewHolder(view)
            }
            MESSAGE_TYPE_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left_side, parent, false)
                MessageViewHolder(view)
            }
            PAYMENT_VIEW_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_request_view_left, parent, false)
                PaymentViewHolder(view)
            }
            PAYMENT_VIEW_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_request_view_right, parent, false)
                PaymentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chatList[position]

        when (holder) {
            is MessageViewHolder -> {
                holder.textUsername.text = chat.message
                holder.datetext.text = chat.date


            }
            is PaymentViewHolder -> {
                holder.amountTextView.text = chat.amount
                holder.messageTextView.text = chat.message
                holder.dateTextView.text = chat.date
                holder.paybtn.setOnClickListener {
                    Log.d("value" , chat.amount.toString())
                    Log.d("value" , chat.upiId.toString())
                    Log.d("value" , chat.username.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textUsername: TextView = view.findViewById(R.id.message)
        val datetext: TextView = view.findViewById(R.id.date)
    }

    class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amountTextView: TextView = view.findViewById(R.id.amountTextView)
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val paybtn: TextView = view.findViewById(R.id.payButton)

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = utilities.getFirebase()
        return when {
            chatList[position].senderId == firebaseUser!!.uid && chatList[position].type == "message" -> MESSAGE_TYPE_RIGHT
            chatList[position].senderId != firebaseUser!!.uid && chatList[position].type == "message" -> MESSAGE_TYPE_LEFT
            chatList[position].senderId == firebaseUser!!.uid && chatList[position].type == "paymentRequest" -> PAYMENT_VIEW_RIGHT
            else -> PAYMENT_VIEW_LEFT
        }
    }
}
