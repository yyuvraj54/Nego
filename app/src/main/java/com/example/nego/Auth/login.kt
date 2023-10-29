package com.example.nego.Auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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


        binding.signupPage.setOnClickListener {
            val intent= Intent(this , signup::class.java);
            startActivity(intent);
        }



        binding.loginbtn.setOnClickListener {

            val email = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()

            Log.d("LoginButton","Clicked CLICKED");
            loginViewModel.login(email, password).observe(this) { loginResponse ->
                // Check if the response is not null
                if (loginResponse != null) {
                    // Access the data in the loginResponse object
                    val success = loginResponse.success
                    val existingUser = loginResponse.existingUser
                    val userId = existingUser.id
                    val userName = existingUser.name
                    val userEmail = existingUser.email

                    // Log or use the data as needed
                    Log.d(TAG, "Success: $success")

                    if(success=="True"){
                      Toast.makeText(this,"Login Successfully",Toast.LENGTH_LONG).show()
                      loginViewModel.saveLoginData(userName,password);
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show()
                    }
//                    Log.d(TAG, "User ID: $userId")
//                    Log.d(TAG, "User Name: $userName")
//                    Log.d(TAG, "User Email: $userEmail")
                } else {
                    // Handle the case where the response is null, indicating an error
                    Log.d(TAG, "Response is null. Handle the error.")
                }
            }
        }
    }
}