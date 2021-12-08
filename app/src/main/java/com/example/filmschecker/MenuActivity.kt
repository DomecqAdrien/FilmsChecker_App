package com.example.filmschecker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun showSearch(v: View?) {
        val i = Intent(this, ShowAllFilmsActivity::class.java)
        startActivity(i)
    }

    fun showFavoris(v: View?) {
        val i = Intent(this, LikedFilmsActivity::class.java)
        startActivity(i)
    }

    fun disconnect(v: View?) {
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        //GoogleSignIn.getLastSignedInAccount(this)
        //mGoogleSignInClient.signOut()
    }
}