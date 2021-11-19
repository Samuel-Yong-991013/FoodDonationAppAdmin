package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val userID = intent.getStringExtra("user_id")
        val emailID = intent.getStringExtra("email_id")

        binding.tvEmailId.text = "Email ID :: $emailID"
        binding.tvUid.text = "User ID :: $userID"

        //OnClickListener for user to logout. Sends then back to the login screen
        binding.logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            val intent =
                Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}