package com.apolis.dec14selfie.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.apolis.dec14selfie.R
import com.apolis.dec14selfie.helpers.SessionManager
import com.apolis.dec14selfie.ui.auth.LoginActivity
import com.apolis.dec14selfie.ui.auth.RegisterActivity
import com.apolis.dec14selfie.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(), View.OnClickListener {

    private val delayedTime:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()
        handler.postDelayed({
            checkLogin()

        },delayedTime)

    }

    override fun onClick(v: View?) {
        when(v){
            login_button->{
                Log.d("abc","clicked")
                startActivity(Intent(this,LoginActivity::class.java))}
            register_button->{
                startActivity(Intent(this,RegisterActivity::class.java))}
            guestIn_button->{
                startActivity(Intent(this,MainActivity::class.java))}
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    private fun checkLogin(){
        var user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            var intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            init()
            splash_progressbar.visibility= View.GONE
        }


    }
    private fun init(){
        login_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        register_button.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        guestIn_button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

}