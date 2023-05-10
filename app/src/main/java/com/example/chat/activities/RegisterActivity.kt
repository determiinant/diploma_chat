package com.example.chat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chat.databinding.ActivityRegisterBinding
import com.example.chat.ui.fragments.EnterPhoneNumberFragment
import com.example.chat.utilits.initFirebase
import com.example.chat.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolBar=mBinding.registerToolbar
        setSupportActionBar(mToolBar)
        title="Ваш телефон"
        replaceFragment(EnterPhoneNumberFragment())
    }
}