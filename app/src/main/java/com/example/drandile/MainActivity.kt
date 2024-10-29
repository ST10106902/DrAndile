package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.drandile.Login // Ensure this matches your actual LoginActivity class name
import com.example.drandile.R // Import your R class


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this matches your layout file name

        // Initialize the Get Started button
        val getStartedButton: Button = findViewById(R.id.get_started)

        // Set onClickListener for the Get Started button
        getStartedButton.setOnClickListener {
            navigateToLogin() // Call the method to navigate to the Login Activity
        }
    }

    // Method to start LoginActivity
    private fun navigateToLogin() {
        val intent = Intent(this,Login::class.java) // Use the correct class name for your Login activity
        startActivity(intent)
        finish() // Optional: Call finish() if you want to close MainActivity
    }
}
