package com.example.nego

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsUtil (context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun saveLoginStatus(isLoggedIn: Boolean) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return sharedPrefs.getBoolean("isLoggedIn", false)
    }

    fun saveUserInfo(
        username: String,
        name: String,
        icon: String,
        id: String,
        password: String,
        phone: String,
        upiId: String,
    ) {
        editor.putString("username", username)
        editor.putString("name", name)
        editor.putString("id", id)
        editor.putString("icon", icon)
        editor.putString("upiId", upiId)
        editor.putString("password", password)
        editor.putString("phone", phone)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPrefs.getString("username", "")
    }
    fun getPassword(): String? {
        return sharedPrefs.getString("password", "")
    }

    fun getId(): String? {
        return sharedPrefs.getString("id", "")
    }
    fun getName(): String? {
        return sharedPrefs.getString("name", "")
    }

    fun getIcon(): String? {
        return sharedPrefs.getString("icon", "")
    }

    fun getUpiId(): String? {
        return sharedPrefs.getString("upiId", "")
    }


    fun getPhone(): String? {
        return sharedPrefs.getString("phone", "")
    }


    fun clearLoginInfo() {
        editor.remove("username")
        editor.remove("password")
        editor.remove("id")
        editor.remove("icon")
        editor.remove("name")
        editor.remove("upiId")
        editor.remove("phone")
        editor.remove("isLoggedIn")
        editor.apply()
    }
}