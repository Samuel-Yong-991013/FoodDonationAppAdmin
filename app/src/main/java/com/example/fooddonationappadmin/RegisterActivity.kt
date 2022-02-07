package com.example.fooddonationappadmin

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var imageUri: Uri
    private lateinit var binding: ActivityRegisterBinding
    private val db = Firebase.firestore

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.uploadUserProfileImageBtn.setOnClickListener{
            selectImage()
        }

        binding.registerBtn.setOnClickListener {

            val registerEmail : String = binding.emailRegister.text.toString().trim()
            val registerPassword : String = binding.passwordRegister.text.toString().trim()

            //Check if register form is empty
            if (!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerPassword)) {
                //Check if given email is of a valid format
                if (Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()) {
                    //Check if userImage have already been selected
                    if(binding.userRegisterProfileImageView.drawable != null){
                        uploadImage()
                    }else{
                        Toast.makeText(this, "Please upload the user's profile image", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Please insert a valid email address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.userRegisterProfileImageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage(){

        val registerEmail : String = binding.emailRegister.text.toString().trim()
        val registerPassword : String = binding.passwordRegister.text.toString().trim()
        val registerUserName: String = binding.userNameRegister.text.toString().trim()

        //New user will be registered
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(registerEmail, registerPassword)
            .addOnCompleteListener(this) { task ->

                //If the registration is successful
                if (task.isSuccessful) {

                    //the current date (down to the seconds) will be used as the file name for the user's profile pic
                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    val fileName = formatter.format(now)

                    storageRef.child("userImages/$fileName")
                        .putFile(imageUri)
                        .addOnSuccessListener {

                            //Firebase registered user
                            val firebaseUser : FirebaseUser = task.result!!.user!!

                            val newAccount = hashMapOf(
                                "email" to registerEmail,
                                "uID" to firebaseUser.uid,
                                "userName" to registerUserName,
                                "profilePath" to "userImages/$fileName",
                                "role" to "user"
                            )

                            db.collection("users")
                                .add(newAccount)
                                .addOnSuccessListener{ documentReference ->
                                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }

                            //sends admin to Home screen
                            val intent =
                                Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        }
                }else{
                    //If register fails, show error message
                    Toast.makeText(
                        this@RegisterActivity,
                        task.exception!!.message.toString(),
                        LENGTH_SHORT
                    ).show()
                }
            }
    }
}