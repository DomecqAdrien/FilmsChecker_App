package com.example.filmschecker.service

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

object FirebaseInstance {

    private var instance: FirebaseInstance? = null

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("288195316574-7q62bf7mpnrhusu8m4popv6clkp0anjf.apps.googleusercontent.com")
        .requestEmail()
        .build()

    fun getInstance(): FirebaseInstance {
        if (instance == null) {
            instance = FirebaseInstance
        }
        return instance as FirebaseInstance
    }
}