package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        usernameEditText = findViewById(R.id.admin_username)
        passwordEditText = findViewById(R.id.admin_password)
        loginButton = findViewById(R.id.admin_login_btn)

        // Set onClickListener for login button
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Check if both fields are filled
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        // Use FirebaseAuth to sign in
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Admin login success, navigate to the Admin Dashboard or HomePage
                    Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Admin::class.java))  // Change this to your Admin Dashboard or appropriate activity
                    finish()
                } else {
                    // If sign-in fails, display a message to the user
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
