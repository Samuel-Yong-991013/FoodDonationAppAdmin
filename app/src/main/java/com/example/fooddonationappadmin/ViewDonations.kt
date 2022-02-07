package com.example.fooddonationappadmin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fooddonationappadmin.databinding.ActivityViewDonationsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class ViewDonations : AppCompatActivity() {
    private lateinit var binding: ActivityViewDonationsBinding
    private val db = Firebase.firestore

    var lv: ListView? = null
    var adapter: SimpleAdapter? = null

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    lateinit var filter : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_donations)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_donations)

        filter = binding.filterViewDonationsSpinner
        val filterOptions = arrayOf("All", "Ongoing", "Rejected", "Completed")

        filter.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterOptions)

        filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewAllDonations()
            }

            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                var selectedFilter = filterOptions[position]

                when(selectedFilter){
                    "All" -> viewAllDonations()
                }
            }

        }
    }

    private fun viewAllDonations() {
        val ar: ArrayList<HashMap<String,Any>> = ArrayList()

        db.collection("donations")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {

                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)

                        lv = binding.viewDonationsList

                        val from = arrayOf(
                            "donationImage",
                            "donationDetails",
                            "donationDate"
                        )
                        val to = intArrayOf(
                            R.id.viewDonationsImageViewListItem,
                            R.id.viewDonationsDetailsListItem,
                            R.id.viewDonationsDateListItem
                        )

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.view_donations_list_item, from, to)

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