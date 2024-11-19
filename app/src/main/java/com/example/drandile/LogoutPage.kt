package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat




class LogoutPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout_page)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Logout Confirmation")
        alertDialog.setMessage("Do you really want to logout?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            performLogout()
        }
        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun performLogout() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
