package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.fooddonationappadmin.databinding.ActivityViewRequestsBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewRequests : AppCompatActivity() {
    private lateinit var binding: ActivityViewRequestsBinding
    private val db = Firebase.firestore

    var lv: ListView? = null
    var adapter: SimpleAdapter? = null

    lateinit var filter : Spinner

    private var counter = 0

    val ar: ArrayList<HashMap<String, Any>> = ArrayList()

    private val from = arrayOf(
        "donationID",
        "requestID",
        "requestDetails",
        "requestAddress",
        "requestStatus",
        "requestDate"
    )
    private val to = intArrayOf(
        R.id.hiddenViewRequestsDonationID,
        R.id.hiddenViewRequestsRequestID,
        R.id.viewRequestsDetailsListItem,
        R.id.viewRequestsAddressListItem,
        R.id.viewRequestsStatusListItem,
        R.id.viewRequestsDateListItem
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_requests)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_requests)

        filter = binding.filterViewRequestsSpinner
        val filterOptions = arrayOf("All", "Pending", "Awaiting delivery", "Complete", "Canceled")

        filter.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterOptions)

        filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewAllRequests()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedFilter = filterOptions[position]

                when(selectedFilter){
                    "All" -> viewAllRequests()
                    "Pending" -> viewPendingRequests()
                    "Awaiting delivery" -> viewAwaitingDeliveryRequests()
                    "Complete" -> viewCompleteRequests()
                    "Canceled" -> viewCanceledRequests()
                }
            }
        }
    }

    private fun viewCanceledRequests(){
        ar.clear()

        db.collection("requests")
            .whereEqualTo("status", "canceled")
            .orderBy("requestDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["donationID"] = doc["donationID"].toString()
                    hashMap["requestID"] = doc.id
                    hashMap["requestDetails"] = doc["requestDetails"].toString()
                    hashMap["requestAddress"] = doc["requestAddress"].toString()
                    hashMap["requestStatus"] = doc["status"].toString()
                    hashMap["requestDate"] = doc["requestDate"].toString()

                    ar.add(hashMap)
                    adapter!!.notifyDataSetChanged()

                    lv = binding.viewRequestsList

                    //adding items to listview
                    adapter =
                        SimpleAdapter(this, ar, R.layout.list_item_view_requests, from, to)

                    lv!!.adapter = adapter
                }
            }
        //onClickListener for list items
        lv!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                val requestID = itemSplit[5]
                val donationID = itemSplit[9]
                val requestStatus = itemSplit[11]

                listViewOnclickListener(requestID, donationID, requestStatus)
            }

        counter += 1
    }

    private fun viewCompleteRequests(){
        ar.clear()

        db.collection("requests")
            .whereEqualTo("status", "completed")
            .orderBy("requestDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["donationID"] = doc["donationID"].toString()
                    hashMap["requestID"] = doc.id
                    hashMap["requestDetails"] = doc["requestDetails"].toString()
                    hashMap["requestAddress"] = doc["requestAddress"].toString()
                    hashMap["requestStatus"] = doc["status"].toString()
                    hashMap["requestDate"] = doc["requestDate"].toString()

                    ar.add(hashMap)
                    adapter!!.notifyDataSetChanged()

                    lv = binding.viewRequestsList

                    //adding items to listview
                    adapter =
                        SimpleAdapter(this, ar, R.layout.list_item_view_requests, from, to)

                    lv!!.adapter = adapter
                }
            }
        //onClickListener for list items
        lv!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                val requestID = itemSplit[5]
                val donationID = itemSplit[9]
                val requestStatus = itemSplit[11]

                listViewOnclickListener(requestID, donationID, requestStatus)
            }

        counter += 1
    }

    private fun viewAwaitingDeliveryRequests(){
        ar.clear()

        db.collection("requests")
            .whereEqualTo("status", "awaiting delivery")
            .orderBy("requestDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["donationID"] = doc["donationID"].toString()
                    hashMap["requestID"] = doc.id
                    hashMap["requestDetails"] = doc["requestDetails"].toString()
                    hashMap["requestAddress"] = doc["requestAddress"].toString()
                    hashMap["requestStatus"] = doc["status"].toString()
                    hashMap["requestDate"] = doc["requestDate"].toString()

                    ar.add(hashMap)
                    adapter!!.notifyDataSetChanged()

                    lv = binding.viewRequestsList

                    //adding items to listview
                    adapter =
                        SimpleAdapter(this, ar, R.layout.list_item_view_requests, from, to)

                    lv!!.adapter = adapter
                }
            }
        //onClickListener for list items
        lv!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                val requestID = itemSplit[5]
                val donationID = itemSplit[9]
                val requestStatus = itemSplit[11]

                listViewOnclickListener(requestID, donationID, requestStatus)
            }

        counter += 1
    }

    private fun viewPendingRequests() {
        ar.clear()

        db.collection("requests")
            .whereEqualTo("status", "pending")
            .orderBy("requestDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["donationID"] = doc["donationID"].toString()
                    hashMap["requestID"] = doc.id
                    hashMap["requestDetails"] = doc["requestDetails"].toString()
                    hashMap["requestAddress"] = doc["requestAddress"].toString()
                    hashMap["requestStatus"] = doc["status"].toString()
                    hashMap["requestDate"] = doc["requestDate"].toString()

                    ar.add(hashMap)
                    adapter!!.notifyDataSetChanged()

                    lv = binding.viewRequestsList

                    //adding items to listview
                    adapter =
                        SimpleAdapter(this, ar, R.layout.list_item_view_requests, from, to)

                    lv!!.adapter = adapter
                }
            }
        //onClickListener for list items
        lv!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                val selectedItem = adapterView.getItemAtPosition(pos).toString()
                val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                val requestID = itemSplit[5]
                val donationID = itemSplit[9]
                val requestStatus = itemSplit[11]

                listViewOnclickListener(requestID, donationID, requestStatus)
            }

        counter += 1
    }

    private fun viewAllRequests() {
        if(counter > 0){
            ar.clear()
        }

        db.collection("requests")
            .orderBy("requestDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["donationID"] = doc["donationID"].toString()
                    hashMap["requestID"] = doc.id
                    hashMap["requestDetails"] = doc["requestDetails"].toString()
                    hashMap["requestAddress"] = doc["requestAddress"].toString()
                    hashMap["requestStatus"] = doc["status"].toString()
                    hashMap["requestDate"] = doc["requestDate"].toString()

                    ar.add(hashMap)
                    //clear ListView
                    if (counter > 0) {
                        adapter!!.notifyDataSetChanged()
                    }

                    lv = binding.viewRequestsList

                    //adding items to listview
                    adapter =
                        SimpleAdapter(this, ar, R.layout.list_item_view_requests, from, to)

                    lv!!.adapter = adapter
                }
                //onClickListener for list items
                lv!!.onItemClickListener =
                    AdapterView.OnItemClickListener { adapterView, view, pos, l ->
                        val selectedItem = adapterView.getItemAtPosition(pos).toString()
                        val itemSplit = selectedItem.split("}", "=", ",", ":").toTypedArray()

                        val requestID = itemSplit[5]
                        val donationID = itemSplit[9]
                        val requestStatus = itemSplit[11]

                        listViewOnclickListener(requestID, donationID, requestStatus)
                    }
            }
    }

    private fun listViewOnclickListener(
        requestID: String,
        donationID: String,
        requestStatus: String
    ) {

        if(requestStatus == "pending" || requestStatus == "canceled"){
            Toast.makeText(this, "There are no donations related to this request", Toast.LENGTH_LONG).show()
        }else{
            val intent =
                Intent(this@ViewRequests, TransactionInfoActivity::class.java)
            intent.putExtra("requestID", requestID)
            intent.putExtra("donationID", donationID)
            startActivity(intent)
        }

    }
}
