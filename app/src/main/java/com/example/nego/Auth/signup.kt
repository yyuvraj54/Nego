package com.example.nego.Auth

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.google.android.gms.cast.framework.media.ImagePicker

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



            binding.signupUserProfile.setOnClickListener {

                loginViewModel.selectImageFromGallery(this,binding.signupUserProfile)

            }

        if (validateFields(name,email,password)) {


                loginViewModel.firebasesignup(name,email,password).observe(this){Response->
                    if (Response != null) {
                        Toast.makeText(this, Response.toString(),Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(this, "Signup Failed",Toast.LENGTH_LONG).show();
                    }
                };

            } else {
                Toast.makeText(this, "Something went wrong,try again  later!", Toast.LENGTH_SHORT).show()
            }




//            loginViewModel.signup(name, email, password).observe(this) { loginResponse ->
//                // Check if the response is not null
//                if (loginResponse != null) {
//                    Toast.makeText(this, "Signup Suceess",Toast.LENGTH_LONG).show();
//
//                }
//                else{
//                    Toast.makeText(this, "Signup Failed",Toast.LENGTH_LONG).show();
//                }
//            }
        }


    }


    private fun validateFields(name :String ,email:String,password:String): Boolean {

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

