package com.example.diswis

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "no_telpon"
        const val KEY_USERNAME = "username"
        const val KEY_ID_USER = "id_user"
    }

    fun createLoginSession(email: String, username: String, noTelpon: String?, idUser: Int) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PHONE, noTelpon)
        editor.putInt(KEY_ID_USER, idUser)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    fun getPhone(): String? {
        return prefs.getString(KEY_PHONE, null)
    }

    fun getIdUser(): Int {
        return prefs.getInt(KEY_ID_USER, -1) // Default -1 if not found
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
