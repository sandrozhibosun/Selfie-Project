package com.apolis.dec14selfie.ui.auth

import androidx.lifecycle.LiveData
import com.apolis.dec14selfie.data.models.User

interface AuthListener {
    fun onSuccess(loginResponse:LiveData<Boolean>)
    fun onFailure(message:String)
    fun onStarted()
}