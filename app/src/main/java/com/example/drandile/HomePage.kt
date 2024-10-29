package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu) // Inflate your menu
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_About_Us -> {
                startActivity(Intent(this, AboutUs::class.java)) // Open About Us activity
                true
            }
            R.id.menu_Our_Centre -> {
                startActivity(Intent(this, OurCentre::class.java)) // Open Our Centre activity
                true
            }
            R.id.menu_Our_Doctors -> {
                startActivity(Intent(this, OurDoctors::class.java)) // Open Our Doctors activity
                true
            }
            R.id.menu_Book_Appointment -> {
                startActivity(Intent(this, BookAppointment::class.java)) // Open Book Appointment activity
                true
            }
            R.id.menu_Admin -> {
                startActivity(Intent(this, Admin::class.java)) // Open Admin activity
                true
            }
            R.id.menu_DashBoard -> {
                startActivity(Intent(this, Dashboard::class.java)) // Open Dashboard activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
