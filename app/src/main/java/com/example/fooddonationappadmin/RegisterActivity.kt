package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialise Firebase Auth
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.registerBtn.setOnClickListener {
            val registerEmail : String = binding.emailRegister.text.toString().trim()
            val registerPassword : String = binding.passwordRegister.text.toString().trim()

            //Check if register form is empty
            if (!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerPassword)) {
                //Check if given email is of a valid format
                if (Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()) {
                    //New user will be registered
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(registerEmail, registerPassword)
                        .addOnCompleteListener(this) { task ->

                            //If the registration is successful
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "The user has been registered successfully",
                                    LENGTH_SHORT
                                ).show()

                                //TEMPORARY SOLUTION
                                //sends user to Login Activity
                                val intent =
                                    Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }else{
                                //If register fails, show error message
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    LENGTH_SHORT
                                ).show()
                            }
                        }
                }else{
                    Toast.makeText(this, "Please insert a valid email address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please insert all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelRegisterButton.setOnClickListener{
            val intent =
                Intent(this@RegisterActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}