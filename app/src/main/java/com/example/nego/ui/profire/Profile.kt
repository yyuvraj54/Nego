package com.example.nego.ui.profire

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nego.Auth.login
import com.example.nego.MainActivity
import com.example.nego.R
import com.example.nego.SharedPrefsUtil
import com.example.nego.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class Profile : Fragment() {
    private lateinit var sharedPrefsUtil: SharedPrefsUtil

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

        binding.logoutBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            Log.d(TAG, "Logout: "+auth.currentUser)
            if(auth.currentUser==null){
                sharedPrefsUtil = SharedPrefsUtil(requireContext())
                sharedPrefsUtil.clearLoginInfo();


                val intent = Intent(requireContext(), login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}