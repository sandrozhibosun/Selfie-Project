package com.apolis.dec14selfie.data.models

data class Image(
    var location:String?=null,
    var uri:String?=null
){
    companion object{
        const val COLLECTION_NAME="IMAGE"
        const val IMAGE_KEY="IMAGES"
    }

}