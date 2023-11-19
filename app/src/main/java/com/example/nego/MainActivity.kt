package com.example.nego

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nego.databinding.ActivityMainBinding
import com.example.nego.ui.profire.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
//        binding.appBarMain.fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val header: View = navView.getHeaderView(0)




        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        val profilePhoto = findViewById<ImageView>(R.id.nav_slide_profile)
//        profilePhoto.setImageResource(R.drawable.avtar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.nav_chats,R.id.nav_home, R.id.nav_slideshow
            ), drawerLayout
        )







        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(firebase.uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var profile= snapshot.child("profileImage").value.toString()
                var username =snapshot.child("userName").value.toString()
                var email =firebase.email.toString()

                val profile_view: CircleImageView = header.findViewById(R.id.nav_slide_profile)
                val username_view: TextView = header.findViewById(R.id.usernameETNAV)
                val email_view: TextView = header.findViewById(R.id.emailETNAV)

                email_view.setText(email)
                username_view.setText(username)

                Glide.with(this@MainActivity)
                    .load(profile)
                    .placeholder(R.drawable.avtar) // Optional: Placeholder image while loading
                    .error(R.drawable.avtar) // Optional: Image to display if loading fails
                    .into(profile_view)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        val updates = hashMapOf<String, Any>(
            "state" to  "online"
        )

        databaseReference!!.updateChildren(updates)



        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> {
//
//                val intent = Intent(this, Profile::class.java)
//                startActivity(intent)
//                finish()
//
//                return true
//            }
//
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    override fun onPause() {
        super.onPause()
        try {
            val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
            val databaseReference =
                FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("User").child(firebase.uid)
            val updates = hashMapOf<String, Any>(
                "state" to "offline"
            )
            databaseReference!!.updateChildren(updates)
        }
        catch (err:Exception){
            Log.d(TAG, "onPause: "+err)}
            }


    override fun onResume() {
        super.onResume()

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance("https://nego-a7774-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(firebase.uid)
        val updates = hashMapOf<String, Any>(
            "state" to  "online"
        )
        databaseReference!!.updateChildren(updates)

    }

}