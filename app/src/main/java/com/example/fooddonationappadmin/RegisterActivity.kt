package com.example.fooddonationappadmin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.fooddonationappadmin.databinding.ActivityMainBinding
import com.example.fooddonationappadmin.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var binding: ActivityRegisterBinding

    private val registerEmail = binding.emailRegister.text.toString()
    private val registerUsername = binding.usernameRegister.text.toString()
    private val registerPassword = binding.passwordRegister.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding.registerBtn.setOnClickListener{
            if(!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerUsername) && !TextUtils.isEmpty(registerPassword)){
                registerUser()
            }else{
                Toast.makeText(this, "Please insert all the fields", Toast.LENGTH_LONG).show()
            }

        }
    }

    //Function for registering user
    fun registerUser(){
//        val registerNewUser = hashMapOf(
//            "email" to registerEmail,
//            "name" to registerUsername,
//            "password" to registerPassword
//        )
//
//        db.collection("users")
//            .add(registerNewUser)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "New user has been created with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error creating new account", e)
//            }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(registerEmail, registerPassword)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    //If registration is successful
                    if (task.isSuccessful) {
                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        Toast.makeText(
                            this@RegisterActivity,
                            "Account registered successfully",
                            Toast.LENGTH_SHORT
                        ).show()


//                        //Here the new user is automatically signed in to the app after registering
//                        val intent =
//                            Intent(this@RegisterActivity, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        intent.putExtra("user_id", firebaseUser.uid)
//                        intent.putExtra("email_id", registerEmail)
//                        startActivity(intent)
//                        finish()
                    } else {
                        //Show error message when registration fails
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }
}