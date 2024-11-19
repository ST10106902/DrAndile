package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var newUserTextView: TextView
    private lateinit var adminLoginTextView: TextView  // Added for Admin login
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        usernameEditText = findViewById(R.id.login_Username_id)
        passwordEditText = findViewById(R.id.login_password_id)
        loginButton = findViewById(R.id.login_btn)
        newUserTextView = findViewById(R.id.textView2)
        adminLoginTextView = findViewById(R.id.adminLoginText)  // Initialize Admin login TextView

        // Set onClickListener for login button
        loginButton.setOnClickListener {
            performLogin()
        }

        // Set onClickListener for new user TextView to go to Register screen
        newUserTextView.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        // Set onClickListener for Admin Login TextView
        adminLoginTextView.setOnClickListener {
            performAdminLogin()
        }
    }

    fun performLogin() {
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
                    // Login success, navigate to the HomePage
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomePage::class.java))  // Changed to HomePage
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun performAdminLogin() {
        val email = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Check if both fields are filled
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password for Admin.", Toast.LENGTH_SHORT).show()
            return
        }

        // For Admin Login, you can add a specific email/password check or role validation
        if (email == "admin@example.com" && password == "adminpassword") {
            // Simulate admin login
            Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Admin::class.java))  // Admin HomePage
            finish()
        } else {
            // If admin credentials don't match, show a failure message
            Toast.makeText(this, "Admin Login Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
