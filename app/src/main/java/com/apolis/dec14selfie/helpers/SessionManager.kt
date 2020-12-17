package com.apolis.dec14selfie.helpers

import android.content.Context
import com.apolis.dec14selfie.data.models.User

class SessionManager(var mContext: Context){

    private val FILE_NAME = "selfie_token_pref"
    private val Key_UserId="UserId"
    private val KEY_NAME = "name"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_Mobile = "mobile"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    var sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun login(user: User): Boolean{

        editor.putString(Key_UserId, user.id)
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putString(KEY_Mobile,user.mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
        return true
    }

    fun getUserName(): String?{
        return sharedPreferences.getString(KEY_NAME, null)
    }
    fun getUserEmail(): String?{
        return sharedPreferences.getString(KEY_EMAIL, null)
    }
    fun getUserId():String?{
        return sharedPreferences.getString(Key_UserId, null)
    }
    fun getUser(): User{
        var userId=sharedPreferences.getString(Key_UserId,null)
        var name = sharedPreferences.getString(KEY_NAME, null)
        var email = sharedPreferences.getString(KEY_EMAIL,null)
        var password = sharedPreferences.getString(KEY_PASSWORD, null)
        var mobile=sharedPreferences.getString(KEY_Mobile,null)
        var user = User(userId!!,name, email, password,mobile)
        return user
    }
    fun logout(){
        editor.clear()
        editor.commit()
    }
}