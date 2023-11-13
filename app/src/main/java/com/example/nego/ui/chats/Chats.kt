package com.example.nego.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nego.Responses.User
import com.example.nego.adapter.UserAdapter
import com.example.nego.databinding.FragmentChatsBinding

class Chats : Fragment() {

    private lateinit var userAdapter: UserAdapter


    private var _binding: FragmentChatsBinding? = null

    // This property is only valid between onCreateView and
    private val binding get() = _binding!!    // onDestroyView.



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatsViewModel = ViewModelProvider(this).get(ChatsViewModel::class.java)

        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.text
//        chatsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        binding.chatrRV.layoutManager = LinearLayoutManager(requireContext())
        chatsViewModel.getUsersListFromRepo().observe(viewLifecycleOwner, { userList ->
            userAdapter = UserAdapter(requireContext(),userList)
            binding.chatrRV.adapter = userAdapter
            userAdapter.notifyDataSetChanged()

        });






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}