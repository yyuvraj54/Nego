package com.example.nego.Auth

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nego.MainActivity
import com.example.nego.R
import com.example.nego.databinding.ActivityLoginBinding
import com.example.nego.databinding.ActivitySignupBinding

class signup : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater);
        setContentView(binding.root);

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)




        binding.signBtn.setOnClickListener {

            val name = binding.nameET.text.toString()
            val email = binding.userET.text.toString()
            val password = binding.passwordET.text.toString()

            loginViewModel.signup(name, email, password).observe(this) { loginResponse ->
                // Check if the response is not null
                if (loginResponse != null) {
                    Toast.makeText(this, "Signup Suceess",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(this, "Signup Failed",Toast.LENGTH_LONG).show();
                }
            }
        }


    }
}

