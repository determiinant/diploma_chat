package com.example.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chat.activities.RegisterActivity
import com.example.chat.databinding.ActivityMainBinding
import com.example.chat.models.User
import com.example.chat.ui.fragments.ChatFragment
import com.example.chat.ui.objects.AppDrawer
import com.example.chat.utilits.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var mToolbar: Toolbar //зв'язок з панеллю інструментів
    lateinit var mAppDrawer : AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser(){
            initFields()
            initFunc()
        }
    }
    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }
    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

    private fun initFunc() {
        if(AUTH.currentUser!=null){
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatFragment(), false)
        }
        else{
            replaceActivity(RegisterActivity())
        }
    }
}