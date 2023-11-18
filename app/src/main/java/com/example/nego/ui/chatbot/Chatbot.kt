package com.example.nego.ui.chatbot

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nego.Responses.chatbotmodel
import com.example.nego.adapter.ChatBotAdapter
import com.example.nego.adapter.UserAdapter
import com.example.nego.databinding.FragmentChatbotBinding


class Chatbot : Fragment() {


    private lateinit var chatBotAdapter : ChatBotAdapter
    private var _binding: FragmentChatbotBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatbotViewModel = ViewModelProvider(this).get(ChatbotViewModel::class.java)

        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chatbotViewModel.chatbotList=ArrayList();
        binding.chatbotRV.layoutManager = LinearLayoutManager(requireContext())





        binding.sendquery.setOnClickListener {


            var query:String  =binding.messagequeryET.text.toString();

            Log.d(TAG, "onCreateView: "+query)
            var value_query=chatbotmodel(query,"person")
            chatbotViewModel.addResponse(value_query)

            chatbotViewModel.callApi("gpt-3.5-turbo-instruct",query,4000,0).observe(viewLifecycleOwner,{gptResponse ->

                if(gptResponse!=null){
                    val choice = gptResponse.choices.firstOrNull()
                    val textInChoice = choice?.text
                    Log.d(TAG, "onCreateView: "+textInChoice)
                    var value_res=chatbotmodel(textInChoice,"BOT")
                    chatbotViewModel.addResponse(value_res)

                }
            });
            addDataToRV( chatbotViewModel.getchatResponse())

            binding.messagequeryET.text.clear()
        }






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun addDataToRV(chatbotList:ArrayList<chatbotmodel>){

        chatBotAdapter = ChatBotAdapter(requireContext(),chatbotList)
        binding.chatbotRV.adapter = chatBotAdapter
        chatBotAdapter.notifyDataSetChanged()

    }




}