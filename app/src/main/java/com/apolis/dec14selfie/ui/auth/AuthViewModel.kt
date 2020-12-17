package com.apolis.dec14selfie.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.apolis.dec14selfie.data.models.User
import com.apolis.dec14selfie.data.repositories.AuthRepository

class AuthViewModel: ViewModel(){

    var email:String?=null
    var password:String?=null
    var authListener:AuthListener?=null
    var name: String? = null
    var emailRegis: String? = null
    var passwordRegis: String? = null
    var mobile: String? = null
    var registerListener: RegisterListener? = null



    fun onLoginButtonClicked(view: View){
        authListener?.onStarted()

        if(email.isNullOrEmpty()||password.isNullOrEmpty()){
            //fail
            authListener?.onFailure("can't be null")
            return
        }
        //to do
        var loginResponse =AuthRepository().loginUser(email!!,password!!)

//
//        if(loginResponse!=null)
        authListener?.onSuccess(loginResponse)
//        else{
//         authListener?.onFailure("password is not match email")
//        }

    }

    fun onRegisterButtonClicked(view: View) {
        registerListener?.onStarted()

        if (emailRegis.isNullOrEmpty() || passwordRegis.isNullOrEmpty() || mobile.isNullOrEmpty() || name.isNullOrEmpty()) {
            //fail
            registerListener?.onFailure("can't be null")
            return
        }
        //to do
        var user = User(name = name, password = passwordRegis, mobile = mobile, email = emailRegis)
        var regisResponse= AuthRepository()
            .registerUser(user)

//        if(registerResponse)
        registerListener?.onSuccess(regisResponse)
//        else{
//            registerListener?.onFailure("email has been registered")
//        }
    }


}