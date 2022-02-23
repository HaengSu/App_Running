package org.techtown.app_running.model

import android.content.Context
import android.content.SharedPreferences

object LoginSharedPreferences {
    private val USER_PROFILE : String = "userProfile"

    fun setUserEmail(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("email", input)
        editor.apply()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        return prefs.getString("email", "").toString()
    }

    fun setUserPass(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("password", input)
        editor.apply()
    }

    fun getUserPass(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        return prefs.getString("password", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}