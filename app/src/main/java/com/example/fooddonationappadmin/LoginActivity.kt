package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityLoginBinding
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val loginEmail = binding.emailLogin.text.toString()
        val loginName = binding.usernameLogin.text.toString()
        val loginPassword = binding.passwordLogin.text.toString()

        //Initialise Firebase Auth
        auth = Firebase.auth

        //Run this function when loginBtn is clicked
        binding.loginBtn.setOnClickListener{
//            startActivity(Intent( this@LoginActivity, MainActivity::class.java))

            //Check if fields are empty
            if( !TextUtils.isEmpty(loginEmail)
                && !TextUtils.isEmpty(loginName)
                && !TextUtils.isEmpty(loginPassword) ) {

                //Function to check if the email is valid
                if(!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
                    loginUser()
                }
            }else{
                Toast.makeText(this@LoginActivity, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    // FUNCTIONS
    private fun loginUser(){

        val loginEmail = binding.emailLogin.text.toString()
        val loginName = binding.usernameLogin.text.toString()
        val loginPassword = binding.passwordLogin.text.toString()

        val email = loginEmail.trim{ it <= ' ' }
        val username = loginName.trim{ it <= ' ' }
        val password = loginPassword.trim{ it <= ' ' }

    }

}