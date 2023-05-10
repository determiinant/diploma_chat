package com.example.chat.models

data class User (
    val id : String = "",
    var username : String = "",
    var description : String = "",
    var phone : String = "",
    var nickname: String = "",
    var photoUrl : String = "empty",
    var state : String = ""

)