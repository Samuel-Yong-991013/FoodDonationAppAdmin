package com.example.fooddonationappadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fooddonationappadmin.databinding.ActivityLoginBinding
import com.example.fooddonationappadmin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    val db = Firebase.firestore

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialise Firebase Auth
        auth = Firebase.auth

        //Run this function when loginBtn is clicked
        binding.loginBtn.setOnClickListener{
            loginUser()
        }
    }

    // FUNCTIONS
    private fun loginUser(){
        var loginEmail = binding.emailLogin.text.toString()
        var loginName = binding.usernameLogin.text.toString()
        var loginPassword = binding.passwordLogin.text.toString()

        db.collection("users")
            .get()
    }
}