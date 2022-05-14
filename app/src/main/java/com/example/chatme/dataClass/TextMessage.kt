package com.example.chatme.dataClass


class TextMessage{

    var text:String? =null
    var senderId:String? =null

    constructor(){}

    constructor(text:String?,uid:String?){
        this.text = text
        this.senderId = uid

    }
}