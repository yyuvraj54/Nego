package com.example.nego.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.nego.MainActivity
import com.example.nego.R
import com.example.nego.databinding.ActivityLoginBinding

class login : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)



        if (loginViewModel.checkLoginStatus()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{



        }


        val email = binding.emailET.text.toString()
        val password = binding.passwordET.text.toString()

        binding.loginbtn.setOnClickListener {
            // Call login method from ViewModel
            loginViewModel.login(email, password).observe(this, { response ->
                // Handle the response here
                if(response != null) {

                } else {
                    // Handle the null response or error
                }
            })
        }
    }
}