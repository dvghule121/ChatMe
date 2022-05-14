package com.example.chatme.dataClass

import android.net.Uri
import android.os.Parcelable

class User{
    var email:String? = null
    var uid:String? = null
    var mobile:String? = null
    var name:String? =null
    var imgUrl:String? = null

    constructor(){}

    constructor(email:String?,uid:String?,mobile:String?,name:String?){
        this.email = email
        this.name = name
        this.mobile = mobile
        this.uid = uid


    }

    constructor(email:String?,uid:String?,mobile:String?,name:String?,imgUrl:String){
        this.email = email
        this.name = name
        this.mobile = mobile
        this.uid = uid
        this.imgUrl = imgUrl


    }
}