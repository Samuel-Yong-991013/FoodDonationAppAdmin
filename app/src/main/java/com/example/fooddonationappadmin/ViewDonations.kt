package com.example.fooddonationappadmin

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityViewDonationsBinding
import com.google.firebase.firestore.Query
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

    lateinit var filter : Spinner

    private var counter = 0

    val ar: ArrayList<HashMap<String,Any>> = ArrayList()

    private val from = arrayOf(
        "requestID",
        "donationID",
        "donationImage",
        "donationDetails",
        "donationStatus",
        "donationDate"
    )
    private val to = intArrayOf(
        R.id.hiddenViewRequestsRequestID,
        R.id.hiddenViewRequestsDonationID,
        R.id.viewDonationsImageViewListItem,
        R.id.viewDonationsDetailsListItem,
        R.id.viewDonationsStatusListItem,
        R.id.viewDonationsDateListItem
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_donations)

        filter = binding.filterViewDonationsSpinner
        val filterOptions = arrayOf("All", "Awaiting confirmation", "Awaiting completion", "Rejected", "Completed", "Canceled", "Failed")

        filter.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterOptions)

        filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewAllDonations()
            }

            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                var selectedFilter = filterOptions[position]

                when(selectedFilter){
                    "All" -> viewAllDonations()
                    "Awaiting confirmation" -> viewAwaitingConfirmationDonations()
                    "Awaiting completion" -> viewAwaitingCompletionDonations()
                    "Rejected" -> viewRejectedDonations()
                    "Completed" -> viewCompletedDonations()
                    "Canceled" -> viewCanceledDonations()
                    "Failed" -> viewFailedDonations()
                }

            }
        }
    }

    private fun viewFailedDonations() {
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "failed")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(this, "No failed donations", Toast.LENGTH_LONG).show()
            }

        counter += 1
    }

    private fun viewCanceledDonations() {
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "canceled")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(this, "No canceled donations", Toast.LENGTH_LONG).show()
            }

        counter += 1
    }

    private fun viewRejectedDonations() {
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "rejected")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(this, "No rejected donations", Toast.LENGTH_LONG).show()
            }

        counter += 1
    }

    private fun viewAwaitingCompletionDonations(){
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "awaiting completion")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(this, "No donations awaiting completion", Toast.LENGTH_LONG).show()
            }

        counter += 1
    }

    private fun viewCompletedDonations() {
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "completed")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Log.w(TAG, "Error writing document", e)
                Toast.makeText(this, "No completed donations", Toast.LENGTH_LONG).show()
            }

        counter += 1
    }

    private fun viewAwaitingConfirmationDonations() {
        //clear listview
        ar.clear()

        db.collection("donations")
            .whereEqualTo("status", "awaiting confirmation")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap:HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        adapter!!.notifyDataSetChanged()

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this, "No donations awaiting confirmation", Toast.LENGTH_LONG).show()
                Log.w(TAG, "Error writing document", e)
            }

        counter += 1
    }

    private fun viewAllDonations() {
        if(counter > 0){
            ar.clear()
        }

        db.collection("donations")
            .orderBy("donationDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {

                    //retrieve imageURI to be displayed on ImageView
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["donationImagePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["requestID"] = doc["requestID"].toString()
                        hashMap["donationID"] = doc.id
                        hashMap["donationImage"] = bitmap
                        hashMap["donationDetails"] = doc["donationDetails"].toString()
                        hashMap["donationStatus"] = doc["status"].toString()
                        hashMap["donationDate"] = doc["donationDate"].toString()

                        ar.add(hashMap)
                        //clear ListView
                        if(counter > 0) {
                            adapter!!.notifyDataSetChanged()
                        }

                        lv = binding.viewDonationsList

                        //Adding items to listview
                        adapter =
                            SimpleAdapter(this, ar, R.layout.list_item_view_donations, from, to)

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
                        //onClickListener for list items
                        lv!!.onItemClickListener =
                            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                                val requestID = itemSplit[3]
                                val donationID = itemSplit[7]

                                listViewOnclickListener(requestID, donationID)
                            }
                    }
                }
            }
    }

    private fun listViewOnclickListener(
        requestID: String,
        donationID: String,
    ) {
        val intent =
            Intent(this@ViewDonations, TransactionInfoActivity::class.java)
        intent.putExtra("requestID", requestID)
        intent.putExtra("donationID", donationID)
        startActivity(intent)
    }

}