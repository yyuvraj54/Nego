package com.example.nego

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nego.Auth.LoginViewModel
import com.example.nego.Responses.Chat
import com.example.nego.adapter.ChatAdapter
import com.example.nego.adapter.UserAdapter
import com.example.nego.databinding.ActivityChatScreenBinding
import com.example.nego.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class chatScreen : AppCompatActivity() {
    private lateinit var ChatScreenViewModel: chatScreenViewModel
    private lateinit var chatAdapter: ChatAdapter
    var firebaseUser:FirebaseUser? =null
    var reference:DatabaseReference? =null
    private lateinit var binding: ActivityChatScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ChatScreenViewModel = ViewModelProvider(this).get(chatScreenViewModel::class.java)


        binding.chatscreenRV.layoutManager=LinearLayoutManager(this, RecyclerView.VERTICAL,false)


        ChatScreenViewModel.updateState()


    binding.backbtn.setOnClickListener{
        onBackPressedDispatcher.onBackPressed()
    }

        var intent =getIntent()
        var userid = intent.getStringExtra("UserId")
        Log.d(ContentValues.TAG, "onBindViewHolder Recived: "+userid)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference =FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(userid!!)

        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user =snapshot.getValue(com.example.nego.Responses.User::class.java)


                binding.chatScreenState.text=user!!.state
               binding.chatScreenName.text =user!!.userName
                if(user.profileImage==""){
                    binding.chatScreenProfileIcon.setImageResource(R.drawable.avtar)
                }
                else{
                    try{
                    Glide.with(this@chatScreen).load(user.profileImage).placeholder(R.drawable.avtar).error(R.drawable.avtar).into(binding.chatScreenProfileIcon) // Optional: Image to display if loading fails
                    }catch (err:Exception){ Log.d(TAG, "onDataChange: "+err)}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        ChatScreenViewModel.readMessages(firebaseUser!!.uid,userid).observe(this, { chatList ->

            chatAdapter = ChatAdapter(this,chatList)
            binding.chatscreenRV.adapter = chatAdapter
            chatAdapter.notifyDataSetChanged()
            binding.chatscreenRV.post {
                scrollToLastItem(binding.chatscreenRV, chatList)
            }

        });


        binding.sendmessageBtn.setOnClickListener {
            var message:String = binding.messageET.text.toString()

            if(message.isEmpty()){
                Toast.makeText(applicationContext,"message is empty",Toast.LENGTH_SHORT).show()
            }

            else{
                ChatScreenViewModel.sendMessage(firebaseUser!!.uid,userid,message)
                binding.messageET.text.clear()
            }




        }

    }
    override fun onResume() {
        super.onResume()

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(firebase.uid)
        val updates = hashMapOf<String, Any>(
            "state" to  "online"
        )
        databaseReference!!.updateChildren(updates)

    }
    private fun scrollToLastItem(recyclerView: RecyclerView ,chatList:ArrayList<Chat>) {
        val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        // Scroll only if there are items in the RecyclerView
        if (chatList.isNotEmpty() && lastVisiblePosition >= 0) {
            recyclerView.scrollToPosition(chatList.size - 1)
        }
    }
    override fun onPause() {
        super.onPause()

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(firebase.uid)
        val updates = hashMapOf<String, Any>(
            "state" to  "offline"
        )
        databaseReference!!.updateChildren(updates)

    }
}