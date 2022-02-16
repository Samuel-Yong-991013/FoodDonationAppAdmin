package com.example.fooddonationappadmin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fooddonationappadmin.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityMainBinding
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var toggle : ActionBarDrawerToggle

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Adding navigation drawer to the Main Activity
        val drawerLayout : DrawerLayout = binding.drawerLayout
        val navView : NavigationView = binding.navView
        var headerView: View = navView.getHeaderView(0)

        var navHeaderLayout = headerView.findViewById<LinearLayout>(R.id.navHeaderLayout)
        var navHeaderUsername = headerView.findViewById<TextView>(R.id.navHeaderUsername)
        var navHeaderEmail = headerView.findViewById<TextView>(R.id.navHeaderEmail)
        var navHeaderProfileImage = headerView.findViewById<ImageView>(R.id.navHeaderProfileImage)

        //set user profile information in the nav_header
        db.collection("admin")
            .whereEqualTo("uID", FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    //retrieve user profile image
                    val storageRef = FirebaseStorage.getInstance().reference.child(doc["profilePath"].toString())
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {

                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                        navHeaderEmail.text = FirebaseAuth.getInstance().currentUser!!.email
                        navHeaderUsername.text = doc["userName"].toString()
                        navHeaderProfileImage.setImageBitmap(bitmap)

                    }
                }
            }

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_viewDonations -> {
                    val intent =
                        Intent(this@MainActivity, ViewDonations::class.java)
                    startActivity(intent)
                }
                R.id.nav_viewRequests -> {
                    val intent =
                        Intent(this@MainActivity, ViewRequests::class.java)
                    startActivity(intent)
                }
                R.id.nav_create_user->{
                    val intent =
                        Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_viewUsers->{
                    val intent =
                        Intent(this@MainActivity, ViewUsersActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    Toast.makeText(this@MainActivity, "You have been logged out", Toast.LENGTH_SHORT).show()
                    Firebase.auth.signOut()
                    val intent =
                        Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
             return true
        }
            return super.onOptionsItemSelected(item)
    }

}