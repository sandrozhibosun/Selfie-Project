package com.apolis.dec14selfie.helpers

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi

fun Context.toast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}
fun Context.d(message: String){
    Log.d("123",message)
}

fun ProgressBar.show(){
    this.visibility= View.VISIBLE
}
fun ProgressBar.hide(){
    this.visibility= View.GONE
}

