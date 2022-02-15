package com.example.fooddonationappadmin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityViewUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ViewUsersActivity : AppCompatActivity() {
    private lateinit var binding : ActivityViewUsersBinding

    //for the listview
    var lv: ListView? = null
    var adapter: SimpleAdapter? = null

    private val db = Firebase.firestore
    private val userAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_users)

        val ar: ArrayList<HashMap<String, Any>> = ArrayList()
        val userAuth = FirebaseAuth.getInstance()

        db.collection("users")
            .whereNotEqualTo("uID", userAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["profilePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        //retrieve userName and email
                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["userName"] = doc["userName"].toString()
                        hashMap["userEmail"] = doc["email"].toString()
                        hashMap["userProfile"] = bitmap

                        ar.add(hashMap)
                        lv = binding.viewUsersListView

                        val from = arrayOf(
                            "userName",
                            "userEmail",
                            "userProfile"
                        )

                        val to = intArrayOf(
                            R.id.viewUsersUsername,
                            R.id.viewUsersEmail,
                            R.id.viewUsersImageView
                        )

                        //Adding items to the listview
                        adapter = SimpleAdapter(this, ar, R.layout.list_item_view_users, from, to)

                        //add image to listview
                        adapter!!.viewBinder = object : SimpleAdapter.ViewBinder {
                            override fun setViewValue(
                                view: View, data: Any?,
                                textRepresentation: String?
                            ): Boolean {
                                if ((view is ImageView) and (data is Bitmap)) {
                                    val donationImage = view as ImageView
                                    val bitmapData = data as Bitmap?
                                    donationImage.setImageBitmap(bitmapData)
                                    return true
                                }
                                return false
                            }
                        }

                        lv!!.adapter = adapter

                    }
                }
            }

    }
}