package com.example.nego

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Utilities {
    fun getFirebase(): FirebaseUser {
        return FirebaseAuth.getInstance().currentUser!!;
    }

}