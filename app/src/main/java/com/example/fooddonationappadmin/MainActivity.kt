package com.example.fooddonationappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fooddonationappadmin.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val userID = intent.getStringExtra("user_id")
        val emailID = intent.getStringExtra("email_id")

        binding.tvEmailId.text = "Email ID :: $emailID"
        binding.tvUid.text = "User ID :: $userID"

        //Adding navigation drawer to the Main Activity
        val drawerLayout : DrawerLayout = binding.drawerLayout
        val navView : NavigationView = binding.navView

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()
                R.id.nav_register -> Toast.makeText(applicationContext, "Fill in the registration form", Toast.LENGTH_SHORT).show()
                R.id.nav_history -> Toast.makeText(applicationContext, "Checking history...", Toast.LENGTH_SHORT).show()
                R.id.nav_track -> Toast.makeText(applicationContext, "Tracking...", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> Toast.makeText(applicationContext, "Logout successful", Toast.LENGTH_SHORT).show()
            }

            true
        }



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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
             return true
        }
            return super.onOptionsItemSelected(item)
    }

}