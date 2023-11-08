package com.example.nego.Auth

import android.content.ContentValues.TAG
import android.content.Context
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

            if(validateFields(this,email,password)){
                loginViewModel.firebaselogin(email, password).observe(this){Response->

                    val (firebaseUserResult, loginStatus) = Response
                    if(loginStatus=="Login Success"){
                        val icon = firebaseUserResult.icon!!
                        val username = firebaseUserResult.username!!
                        val uid = firebaseUserResult.uid!!
                        loginViewModel.saveLoginData(email,username,icon,uid, password)
                        Toast.makeText(this, Response.toString(),Toast.LENGTH_LONG).show();
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, loginStatus.toString(),Toast.LENGTH_LONG).show();
                    }

                    }


                }
            }

//            Log.d("LoginButton","Clicked CLICKED");
//            loginViewModel.login(email, password);
        }

    private fun validateFields(content:Context,email:String,password:String): Boolean {

        if (email.isEmpty()) {
            // Show a toast message for empty email
            Toast.makeText(content, "Please enter an email", Toast.LENGTH_SHORT).show()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Show a toast message for invalid email format
            Toast.makeText(content, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            // Show a toast message for empty password
            Toast.makeText(content, "Please enter a password", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 6) {
            // Show a toast message for password length less than 6 characters
            Toast.makeText(content, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // If all validations pass, return true for success
        return true
    }

}