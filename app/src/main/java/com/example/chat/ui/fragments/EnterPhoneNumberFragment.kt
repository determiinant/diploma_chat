package com.example.chat.ui.fragments

import androidx.fragment.app.Fragment

import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.activities.RegisterActivity
import com.example.chat.utilits.AUTH
import com.example.chat.utilits.replaceActivity
import com.example.chat.utilits.replaceFragment
import com.example.chat.utilits.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber : String
    private lateinit var mCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                AUTH.signInWithCredential(p0).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast("Ласкаво просимо!")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    }
                    else showToast(it.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
        button_next.setOnClickListener{sendCode()}
    }

    private fun sendCode() {
        if(input_phone_number.text.toString().isEmpty())
            showToast("Введіть номер телефону!")
        else{
                authUser()
            }
    }

    private fun authUser() {
        mPhoneNumber = input_phone_number.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            mCallback
        )
    }
}