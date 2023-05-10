package com.example.chat.ui.fragments

import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.utilits.USER
import com.example.chat.utilits.downloadAndSetImage
import com.example.chat.utilits.replaceFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.profile_user_photo

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        button_edit.setOnClickListener { (activity as MainActivity).replaceFragment(EditProfileFragment()) }
        profile_full_name.text = USER.username
        profile_user_description.text = USER.description
        profile_user_photo.downloadAndSetImage(USER.photoUrl)

    }
}