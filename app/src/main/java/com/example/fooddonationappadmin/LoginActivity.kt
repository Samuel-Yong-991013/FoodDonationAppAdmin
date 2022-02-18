package com.example.fooddonationappadmin

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityLoginBinding

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Check if the user is already signed in reroute to MainActivity, otherwise, login
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            // User is not signed in
            Log.d(ContentValues.TAG, "onAuthStateChanged:signed_out")
        }

        //Initialise Firebase Auth
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //Run this function when loginBtn is clicked
        binding.loginBtn.setOnClickListener{

            val loginEmail : String = binding.emailLogin.text.toString().trim()
            val loginPassword : String = binding.passwordLogin.text.toString().trim()

            //Check if fields are empty
            if( !TextUtils.isEmpty(loginEmail)
                && !TextUtils.isEmpty(loginPassword) ) {

                    db.collection("admin")
                        .whereEqualTo("email", loginEmail)
                        .get()
                        .addOnSuccessListener { users ->
                            for(user in users){
                                if(!user.exists()){
                                    Toast.makeText(this, "Account does not exist", Toast.LENGTH_SHORT).show()
                                } else{
                                    //If fields are filled in, attempt to log in the user
                                    auth.signInWithEmailAndPassword(loginEmail, loginPassword)
                                        .addOnCompleteListener { task ->
                                            //Show message that login is successful and move user to MainActivity
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    "You are logged in successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                val intent =
                                                    Intent(this@LoginActivity, MainActivity::class.java)
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                intent.putExtra(
                                                    "user_id",
                                                    FirebaseAuth.getInstance().currentUser!!.uid
                                                )
                                                intent.putExtra("email_id", loginEmail)
                                                startActivity(intent)
                                                finish()

                                            }else{
                                                //Show error message if login fails
                                                Toast.makeText(
                                                    this@LoginActivity,
                                                    task.exception!!.message.toString(),
                                                    LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                        }.addOnFailureListener{ e -> Log.w(TAG, "Error writing document", e) }
            }else{
                //If the fields are empty
                Toast.makeText(this@LoginActivity, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

}