package com.example.chat.utilits

enum class AppStates(val state: String) {
    ONLINE("В мережі"),
    OFFLINE("Не в мережі"),
    TYPING("Друкує...");

    companion object{
        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                .setValue(appStates.state)
                .addOnSuccessListener { USER.state = appStates.state }
                .addOnFailureListener{
                    showToast(it.message.toString())
                }
        }
    }
}