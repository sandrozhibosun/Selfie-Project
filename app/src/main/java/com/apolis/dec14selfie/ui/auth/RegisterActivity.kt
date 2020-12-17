package com.apolis.dec14selfie.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apolis.dec14selfie.R
import com.apolis.dec14selfie.databinding.ActivityRegisterBinding
import com.apolis.dec14selfie.helpers.d
import com.apolis.dec14selfie.helpers.hide
import com.apolis.dec14selfie.helpers.show
import com.apolis.dec14selfie.helpers.toast
import com.apolis.dec14selfie.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity(),RegisterListener {
    lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        var authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        mBinding.viewmodel=authViewModel
        authViewModel.registerListener=this
        progress_bar.hide()
    }

    override fun onSuccess(regisResponse:LiveData<Boolean>) {

        progress_bar.hide()
        regisResponse.observe(this, Observer {
            if(it){
                this.toast("register Success")
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else{
                this.toast("register fail")
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