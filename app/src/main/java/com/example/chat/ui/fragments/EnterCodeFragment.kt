package com.example.chat.ui.fragments

import androidx.fragment.app.Fragment
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.activities.RegisterActivity
import com.example.chat.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        input_code.addTextChangedListener(AppTextWatcher{
                val str = input_code.text.toString()
                if(str.length==6){
                    enterCode()
                }
        })
    }
    private fun enterCode() {
        val code = input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val uid = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME] = uid

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast("Ласкаво просимо!")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        }
                        else{
                            showToast(it.exception?.message.toString())
                        }
                }
            }
            else showToast(it.exception?.message.toString())
        }
    }
}