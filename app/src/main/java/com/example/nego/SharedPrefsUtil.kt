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

    fun saveUserInfo(username: String, password: String) {
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPrefs.getString("username", "")
    }
    fun getPassword(): String? {
        return sharedPrefs.getString("password", "")
    }

    fun clearLoginInfo() {
        editor.remove("username")
        editor.remove("password")
        editor.remove("isLoggedIn")
        editor.apply()
    }
}