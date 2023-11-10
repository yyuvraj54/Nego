package com.example.nego.Auth

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nego.databinding.ActivitySignupBinding
import com.example.nego.firebase.FirebaseStorage


class signup : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivitySignupBinding
    var icon: Uri? =null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            if (selectedImage != null) {
                icon= selectedImage
                // Load the selected image into your CircleImageView using an image loading library
                // For example, using Glide:
                Glide.with(this)
                    .load(selectedImage)
                    .into(binding.signupUserProfile)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater);
        setContentView(binding.root);

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)







        binding.signBtn.setOnClickListener {
            binding.signBtn.isEnabled = false
            val name = binding.nameET.text.toString()
            val email = binding.userET.text.toString()
            val password = binding.passwordET.text.toString()




        if (validateFields(name,email,password)) {
            if(icon!=null){

                val uploader = FirebaseStorage()
                uploader.uploadImage(selectedImageUrl = icon!!,
                    uploadPath = "${email}/profile", // adjust the path as needed
                    listener = object : FirebaseStorage.OnImageUploadListener {
                        override fun onImageUploadSuccess(downloadUrl: String) {

                            println("Image uploaded successfully. Download URL: $downloadUrl")
                            var profileIcon = downloadUrl

                            loginViewModel.firebasesignup(name,email,password,profileIcon).observe(this@signup){Response->
                                if (Response != null) {
                                    Toast.makeText(this@signup, Response.toString(),Toast.LENGTH_SHORT).show();
                                    binding.signBtn.isEnabled = true
                                }
                                else{
                                    Toast.makeText(this@signup, "Signup Failed",Toast.LENGTH_SHORT).show();
                                    binding.signBtn.isEnabled = true
                                }
                            };
                        }

                        override fun onImageUploadFailure(exception: Exception) {
                            Toast.makeText(this@signup, "Signup Failed : Profile Image Can't be set!",Toast.LENGTH_LONG).show();
                            binding.signBtn.isEnabled = true
                        }
                    }
                )

            }

            else{
                val defaultImage=loginViewModel.getProfileImage();
                loginViewModel.firebasesignup(name,email,password,defaultImage).observe(this@signup){Response->
                    if (Response != null) {
                        Toast.makeText(this@signup, Response.toString(),Toast.LENGTH_SHORT).show();
                        binding.signBtn.isEnabled = true
                    }
                    else{
                        Toast.makeText(this@signup, "Signup Failed",Toast.LENGTH_SHORT).show();
                        binding.signBtn.isEnabled = true
                    }
                };

            }

            } else {
                Toast.makeText(this, "Something went wrong,try again  later!", Toast.LENGTH_SHORT).show()
                binding.signBtn.isEnabled = true
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


            binding.signupUserProfile.setOnClickListener {

            Log.d(TAG, "onCreate:  clicked clicked")
            loginViewModel.selectImageFromGallery(this,binding.signupUserProfile)


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

