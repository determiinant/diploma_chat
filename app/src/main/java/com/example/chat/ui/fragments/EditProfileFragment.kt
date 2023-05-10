package com.example.chat.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.utilits.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.profile_user_photo
import kotlinx.android.synthetic.main.fragment_profile.*

class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile) {
    lateinit var mNewNickname : String

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        editTextUserName.setText(USER.username)
        editTextNick.setText(USER.nickname)
        editTextDescription.setText(USER.description)
        editTextPhoneNumber.setText(USER.phone)
        profile_user_photo.downloadAndSetImage(USER.photoUrl)
        profile_add_photo.setOnClickListener { changeUserPhoto() }

    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGES)
                .child(UID)

            putImageToStorage(uri, path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        profile_user_photo.downloadAndSetImage(it)
                        showToast("Фотографія додана")
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.confirm_changes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.confirm_changes_button -> changeInfoAboutUser()
        }
        return true
    }

    private fun changeInfoAboutUser() {
        val username = editTextUserName.text.toString()
        mNewNickname = editTextNick.text.toString()
        val description = editTextDescription.text.toString()
        //val phone = editTextPhoneNumber.text.toString()

        if(mNewNickname.isEmpty()){
            showToast("Нікнейм не може бути порожнім")
        }
        else{
            REF_DATABASE_ROOT.child(NODE_NICKNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewNickname)){
                        showToast("Такий користувач вже існує в базі даних")
                    }
                    else{
                        REF_DATABASE_ROOT.child(NODE_NICKNAMES).child(mNewNickname)
                            .setValue(UID).addOnCompleteListener {
                                if(it.isSuccessful){
                                    updateCurrentNickname()
                                }
                            }
                    }
                })
        }
        if(username.isEmpty()){
            showToast("Ім'я не може бути порожнім")
        }
        else{
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
                .setValue(username).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast("Ім'я успішно змінено")
                        USER.username = username
                    }
                    else{
                        showToast(it.exception?.message.toString())
                    }
                }
        }
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_DESCRIPTION)
            .setValue(description).addOnCompleteListener {
                if(it.isSuccessful){
                    showToast("Опис успішно змінено")
                    USER.description = description
                }
            }
        fragmentManager?.popBackStack()
    }

    private fun updateCurrentNickname() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_NICKNAME)
            .setValue(mNewNickname).addOnCompleteListener {
                if(it.isSuccessful){
                    showToast("Нікнейм успішно змінений")
                    REF_DATABASE_ROOT.child(NODE_NICKNAMES).child(USER.nickname)
                        .removeValue()
                        .addOnCompleteListener {
                        if(it.isSuccessful)
                            USER.nickname = mNewNickname
                    }
                }
                else{
                    showToast(it.exception?.message.toString())
                }
            }
    }
}