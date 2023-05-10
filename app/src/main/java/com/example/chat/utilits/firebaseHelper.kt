package com.example.chat.utilits

import android.net.Uri
import com.example.chat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH : FirebaseAuth
lateinit var UID : String
lateinit var REF_DATABASE_ROOT : DatabaseReference
lateinit var REF_STORAGE_ROOT : StorageReference
lateinit var USER : User

const val NODE_NICKNAMES = "nicknames"

const val FOLDER_PROFILE_IMAGES = "profile_images"

const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_DESCRIPTION = "description"
const val CHILD_NICKNAME = "nickname"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER =User()
    UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}
inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URL)
        .setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener{showToast(it.message.toString())}
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url : String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())  }
        .addOnFailureListener{showToast(it.message.toString())}
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {  function() }
        .addOnFailureListener{showToast(it.message.toString())}
}

inline fun initUser(crossinline function: () -> Unit){
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener{
            USER = it.getValue(User::class.java) ?:User()
            if(USER.nickname.isEmpty()){
                USER.nickname = UID
            }
            function()
        })
}