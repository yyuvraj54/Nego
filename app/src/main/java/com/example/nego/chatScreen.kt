package com.example.nego

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nego.Responses.Chat
import com.example.nego.adapter.ChatAdapter
import com.example.nego.databinding.ActivityChatScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import java.util.UUID

class chatScreen : AppCompatActivity(),ChatAdapter.OnItemClickListener {
    private lateinit var ChatScreenViewModel: chatScreenViewModel
    private val utilities = Utilities()
    public  lateinit var  phone:String
    public  lateinit var  user:String
    public  lateinit var  upiId:String
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

        var userid = intent.getStringExtra("UserId")



    binding.backbtn.setOnClickListener{
        onBackPressedDispatcher.onBackPressed()
    }




        val sharedPrefsUtil = SharedPrefsUtil(this)
        phone = sharedPrefsUtil.getPhone().toString()
        upiId = sharedPrefsUtil.getUpiId().toString()

        Log.d(ContentValues.TAG, "onBindViewHolder Recived: "+userid)

        firebaseUser = utilities.getFirebase()
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

            }

        })




        ChatScreenViewModel.readMessages(firebaseUser!!.uid, userid!!).observe(this, { chatList ->

            chatAdapter = ChatAdapter(this,chatList,this)
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
                ChatScreenViewModel.sendMessage(firebaseUser!!.uid, userid!!,message)
                binding.messageET.text.clear()
            }




        }
        binding.requestPaymentButton.setOnClickListener{
            showPaymentRequestPopup()
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

    private fun showPaymentRequestPopup() {
        val dialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.popup_payment_request, null)
        dialog.setContentView(view)

        val amountEditText: EditText = view.findViewById(R.id.amountEditText)
        val messageEditText: EditText = view.findViewById(R.id.messageEditText)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val requestPaymentButton: Button = view.findViewById(R.id.requestPaymentButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        requestPaymentButton.setOnClickListener {
            // Handle payment request logic here
            var amount = amountEditText.text.toString()
            amount = "₹"+ amount
            val message = messageEditText.text.toString()
            var userid = intent.getStringExtra("UserId")

            val sharedPrefsUtil = SharedPrefsUtil(this)
            user = sharedPrefsUtil.getName().toString()


            if (userid != null) {
                ChatScreenViewModel.sendPaymentMessage(firebaseUser!!.uid,userid,message,amount = amount,phone =  phone, upiId = upiId, username = user)
            }

            binding.messageET.text.clear()
            // Perform your action with the amount and message
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun formatAmount(amount: String): String {
        val cleanedAmount = amount.replace("₹", "").trim()
        val amountAsDouble = cleanedAmount.toDoubleOrNull() ?: 0.0
        return String.format("%.2f", amountAsDouble)
    }

    fun generateRandomTransactionId(): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15)
    }

    fun generateRandomMerchantCode(): String {
        return (10000..99999).random().toString()
    }

    override fun onButtonClick(details: Chat, context: Context) {

        Log.d(TAG, details.username+" "+ details.amount+" "+ details.message+" "+ details.upiId)


            val easyUpiPayment = EasyUpiPayment(this@chatScreen) {
                payeeVpa = details.upiId.toString()
                paymentApp = PaymentApp.ALL
                payeeName = details.username.toString()
                payeeMerchantCode = generateRandomMerchantCode()
                transactionId = generateRandomTransactionId()
                transactionRefId = generateRandomTransactionId().toString()
                description = details.message.toString()
                amount = formatAmount(details.amount.toString())
            }

            easyUpiPayment.startPayment()

    }



}