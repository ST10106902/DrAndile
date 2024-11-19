package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        emailInput = findViewById(R.id.admin_email_input)
        passwordInput = findViewById(R.id.admin_password_input)
        loginButton = findViewById(R.id.admin_login_button)

        // Set onClickListener for login button
        loginButton.setOnClickListener {
            loginAdmin()
        }
    }

    private fun loginAdmin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Authenticate admin
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Verify admin role
                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid
                        val database = FirebaseDatabase.getInstance().reference.child("users").child(uid)
                        database.child("role").get().addOnSuccessListener { snapshot ->
                            val role = snapshot.getValue(String::class.java)
                            if (role == "admin") {
                                // Redirect to Admin Page
                                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, Admin::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Access Denied: Not an admin", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to verify user role", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
