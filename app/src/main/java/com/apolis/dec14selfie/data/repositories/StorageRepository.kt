package com.apolis.dec14selfie.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apolis.dec14selfie.data.models.Image
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class StorageRepository {

    var mStorageRef: StorageReference= FirebaseStorage.getInstance().getReference();
    var databaseReference= FirebaseDatabase.getInstance().getReference(Image.COLLECTION_NAME)

    fun readAllImage(){


    }
    fun uploadImageToFireStorage(selectedImage: Uri?)
    {
                if(selectedImage==null){
            return
        }
        val location=selectedImage.lastPathSegment?:"name.jpg"
        var ref=mStorageRef.
        child("images/"+ UUID.randomUUID().toString())
        ref.putFile(selectedImage).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                Log.d("123","downloadUrl:${it.toString()}")
                addSelfie(Image(location,it.toString()))

            }

        }
            .addOnFailureListener{

            }

    }
    fun addSelfie(image: Image){
        val key =databaseReference.push().key
        databaseReference.child(key!!).setValue(image)
    }
    fun getImage():MutableLiveData<ArrayList<Image>>{
        var result=MutableLiveData<ArrayList<Image>>()
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val mList=ArrayList<Image>()
                for(data in snapshot.children)
                {
                    mList.add(data.getValue(Image::class.java)!!)
                }
                result.value=mList
            }

        })
        return result
    }


}