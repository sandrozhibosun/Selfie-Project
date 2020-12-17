package com.apolis.dec14selfie.data.repositories

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apolis.dec14selfie.data.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AuthRepository {
    var databaseReference = FirebaseDatabase.getInstance().getReference(User.COLLECTION_NAME)
    var res:User?=null

    fun loginUser(email:String,password:String):MutableLiveData<Boolean>{

        var livedataFlag=MutableLiveData<Boolean>()
        var auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if(it.isSuccessful)
                {
                    livedataFlag.value=true
                   return@addOnCompleteListener
                }
                else{
                    livedataFlag.value=false
//
                }
            }
        return livedataFlag




    }



//    fun registerUser(user: User): Boolean {
//        var flag=true
//        var firebaseDatabase = FirebaseDatabase.getInstance()
//        var databaseReference = firebaseDatabase.getReference("users")
//        var userId = databaseReference.push().key
//        databaseReference.addValueEventListener(object: ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(data in snapshot.children){
//                    if(data!=null){
//                        var dataUser =data.getValue(User::class.java)
//
//                        if(dataUser!!.email==user.email)
//                        {
//                            flag=false
//                        }
//
//                    }
//                }
//            }
//
//        })
//        if(!flag)
//        {
//            return flag
//        }
//
//        databaseReference.child(userId!!).setValue(user)
//        return true
//
//    }
    fun registerUser(user:User):MutableLiveData<Boolean>{
    var auth = FirebaseAuth.getInstance()
    var livedataFlag=MutableLiveData<Boolean>()


    auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
        .addOnCompleteListener{
            if(it.isSuccessful)
            {
                livedataFlag.value=true
                return@addOnCompleteListener

            }
            else{
               livedataFlag.value=false
            }
        }
    return livedataFlag
        }
}

