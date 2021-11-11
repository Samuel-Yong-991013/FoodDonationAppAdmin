package com.example.fooddonationappadmin

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fooddonationappadmin.databinding.ActivityMainBinding
import com.example.fooddonationappadmin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding.registerBtn.setOnClickListener{
            registerUser()
        }
    }

    fun registerUser(){
        var registerEmail = binding.emailRegister.text.toString()
        var registerUsername = binding.usernameRegister.text.toString()
        var registerPassword = binding.passwordRegister.text.toString()

        val registerNewUser = hashMapOf(
            "email" to registerEmail,
            "name" to registerUsername,
            "password" to registerPassword
        )

        db.collection("users")
            .add(registerNewUser)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "New user has been created with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating new account", e)
            }
    }
}