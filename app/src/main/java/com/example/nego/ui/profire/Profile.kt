package com.example.nego.ui.profire

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nego.R
import com.example.nego.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User


class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root




        profileViewModel.getData().observe(viewLifecycleOwner){userList->
            if (userList != null && userList.isNotEmpty()) {
                // Accessing the first user in the list
                val user = userList[0]

                val name = user.userName
                val userId = user.userId
                val profileIconUrl = user.profileImage // Assuming profileIcon is a URL

                binding.profileName.text =name
                val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
                Glide.with(this)
                    .load(profileIconUrl)
                    .placeholder(R.drawable.avtar) // Optional: Placeholder image while loading
                    .error(R.drawable.avtar) // Optional: Image to display if loading fails
                    .into(binding.profileImage)
                binding.profileEmail.text =firebase.email
            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}