package com.apolis.dec14selfie.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import com.apolis.dec14selfie.data.models.Image

interface MainListener {
    fun onGet(mList:LiveData<ArrayList<Image>>)
    fun uploadData(uri: Uri)
}