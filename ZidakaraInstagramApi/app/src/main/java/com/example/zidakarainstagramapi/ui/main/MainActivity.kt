package com.example.zidakarainstagramapi.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.zidakarainstagramapi.ui.collection.MyZidakaraCollectionList
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.ui.content.ZidakaraContent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    companion object {
        const val RC_SIGN_IN = 101
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showSignIn()
        }
    }

    private fun showSignIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
            } else {
                if (response == null) {
                    finish()
                } else {
                    response.error?.errorCode?.let { error ->
                        when (error) {
                            ErrorCodes.NO_NETWORK -> Toast.makeText(
                                this,
                                "No connection",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            else -> Toast.makeText(
                                this,
                                "An error occurred please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.zidakara_content.setOnClickListener {
            val intent = Intent(this, ZidakaraContent::class.java)
            startActivity(intent)

        }
        
        logout.setOnClickListener{ 
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
                showSignIn()
            }
        }

        my_zidakara_collection.setOnClickListener {
            val intent = Intent(this, MyZidakaraCollectionList::class.java)
            startActivity(intent)
        }
    }
}