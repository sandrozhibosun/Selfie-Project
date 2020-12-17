package com.apolis.dec14selfie.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apolis.dec14selfie.data.models.Image
import com.apolis.dec14selfie.data.repositories.StorageRepository
import com.apolis.dec14selfie.helpers.d
import com.apolis.dec14selfie.helpers.toast
import java.util.*
import kotlin.collections.ArrayList

class SelfieViewModel :ViewModel(){
    var mainListener:MainListener?=null
    var mList:LiveData<ArrayList<Image>> = MutableLiveData()
    var storageRepository=StorageRepository()

     fun uploadImage(selectedImage: Uri?){
        storageRepository.uploadImageToFireStorage(selectedImage)

    }
    fun getImagesList(){
        mList=storageRepository.getImage()
        mainListener!!.onGet(mList)
    }

}