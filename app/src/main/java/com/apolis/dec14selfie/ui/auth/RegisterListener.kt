package com.apolis.dec14selfie.ui.auth

import androidx.lifecycle.LiveData

interface RegisterListener {
    fun onSuccess(regisResponse:LiveData<Boolean>)
    fun onFailure(message:String)
    fun onStarted()
}