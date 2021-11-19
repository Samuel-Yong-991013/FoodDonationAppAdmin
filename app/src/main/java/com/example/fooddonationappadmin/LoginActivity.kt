package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialise Firebase Auth
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

//        val loginEmail : String = binding.emailLogin.text.toString()
//        val loginPassword : String = binding.passwordLogin.text.toString()

        val loginEmail : String = "samuelyong91@gmail.com"
        val password : String = "123456"

//        val loginEmail = findViewById<EditText>(R.id.emailLogin).text.toString()
//        val loginPassword = findViewById<EditText>(R.id.passwordLogin).text.toString()

        //Run this function when loginBtn is clicked
        binding.loginBtn.setOnClickListener{
            //Check if fields are empty
            if( !TextUtils.isEmpty(binding.emailLogin.text)
                && !TextUtils.isEmpty(binding.passwordLogin.text) ) {

                //If fields are filled in, attempt to log in the user
                auth.signInWithEmailAndPassword(loginEmail, password)
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
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }else{
                //If the fields are empty
                Toast.makeText(this@LoginActivity, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

}