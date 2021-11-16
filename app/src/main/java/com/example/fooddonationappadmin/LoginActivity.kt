package com.example.fooddonationappadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
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

    private val loginEmail = binding.emailLogin.text.toString()
    private val loginName = binding.usernameLogin.text.toString()
    private val loginPassword = binding.passwordLogin.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialise Firebase Auth
        auth = Firebase.auth

        //Run this function when loginBtn is clicked
        binding.loginBtn.setOnClickListener{
            //Check if fields are empty
            if(!TextUtils.isEmpty(loginEmail.trim{ it <= ' '})
                && !TextUtils.isEmpty(loginName.trim{ it <= ' '})
                && !TextUtils.isEmpty(loginPassword.trim{ it <= ' '})) {

                //Function to check if the email is valid
                if(!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
                    loginUser()
                }
            }else{
                Toast.makeText(this, "Please insert all the fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    // FUNCTIONS
    private fun loginUser(){
        val email = loginEmail.trim{ it <= ' ' }
        val username = loginName.trim{ it <= ' ' }
        val password = loginPassword.trim{ it <= ' ' }

    }
}