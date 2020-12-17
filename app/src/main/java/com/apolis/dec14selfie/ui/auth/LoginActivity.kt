package com.apolis.dec14selfie.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apolis.dec14selfie.R
import com.apolis.dec14selfie.data.models.User
import com.apolis.dec14selfie.databinding.ActivityLoginBinding
import com.apolis.dec14selfie.helpers.*
import com.apolis.dec14selfie.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),AuthListener {
    lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        var authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        mBinding.viewmodel=authViewModel
        authViewModel.authListener=this
        progress_bar.hide()
    }

    override fun onSuccess(loginResponse:LiveData<Boolean>) {
        progress_bar.hide()
        loginResponse.observe(this, Observer {
           if(it){
               this.toast("login Success")
               startActivity(Intent(this,MainActivity::class.java))
           }
            else{
               this.toast("login fail")
           }
        })

    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        this.toast(message)
        this.d(message)
    }

    override fun onStarted() {
        progress_bar.show()
    }
}