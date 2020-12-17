package com.apolis.dec14selfie.data.models

import java.io.Serializable

data class User(var id:String?=null,
                var name: String?=null,
                var email: String?=null,
                var mobile: String?=null,
                var password: String?=null):Serializable
{
    companion object{
        const val COLLECTION_NAME = "users"
        const val User_Key="USER"
    }

}
