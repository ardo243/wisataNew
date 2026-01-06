package com.example.diswis.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun createLoginSession(email: String, nama: String) {
        prefs.edit()
            .putBoolean("isLogin", true)
            .putString("email", email)
            .putString("nama", nama)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLogin", false)
    }

    fun getEmail(): String? {
        return prefs.getString("email", "")
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}

