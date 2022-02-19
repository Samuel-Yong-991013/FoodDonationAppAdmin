package com.example.fooddonationappadmin

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityTransactionInfoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TransactionInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionInfoBinding
    private var db = Firebase.firestore

    var userName = ""
    var number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_info)

        var requestID = intent.getStringExtra("requestID").toString()
        var donationID = intent.getStringExtra("donationID").toString()

        //fill in request info
        db.collection("requests").document(requestID)
            .get()
            .addOnSuccessListener { req ->

                db.collection("users")
                    .whereEqualTo("uID", req["uID"].toString())
                    .get()
                    .addOnSuccessListener { users ->
                        for(user in users){
                            userName = user["userName"].toString()
                            binding.requestInfoRequestDetails.text = req["requestDetails"].toString()
                            binding.requestInfoRecipientName.text = userName
                            binding.requestInfoRequestAddress.text = req["requestAddress"].toString()
                            binding.requestInfoRequestDate.text = req["requestDate"].toString()
                            binding.requestInfoPreferredDeliveryDate.text = req["requestPreferredDate"].toString()
                            binding.requestInfoPreferredDeliveryTime.text = req["requestPreferredTime"].toString()
                        }
                    }
            }

        //fill in donation info
        db.collection("donations").document(donationID)
            .get()
            .addOnSuccessListener { don ->
                db.collection("users")
                    .whereEqualTo("uID", don["uID"].toString())
                    .get()
                    .addOnSuccessListener { users ->
                        for(user in users){
                            userName = user["userName"].toString()
                            number = user["phoneNum"].toString()

                            //set image into imageView
                            val storageRef = FirebaseStorage.getInstance().reference.child(don["donationImagePath"].toString())
                            val localFile = File.createTempFile("tempImage", "jpg")
                            storageRef.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                                binding.requestInfoImageView.setImageBitmap(bitmap)
                                binding.requestInfoDonationDetails.text = don["donationDetails"].toString()
                                binding.requestInfoDonationDate.text = don["donationDate"].toString()
                                binding.requestInfoDonationComments.text = don["donationComments"].toString()
                                binding.requestInfoDonorName.text = userName
                                binding.requestInfoDonorNumber.text = number
                            }
                        }
                    }
            }
    }
}