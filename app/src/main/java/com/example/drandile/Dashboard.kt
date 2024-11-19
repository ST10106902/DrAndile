package com.example.drandile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var appointmentListView: ListView
    private lateinit var logoutButton: Button
    private lateinit var database: DatabaseReference
    private val appointments = mutableListOf<Appointment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize UI components
        appointmentListView = findViewById(R.id.appointmentListView)
        logoutButton = findViewById(R.id.button3)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("appointments")

        // Retrieve appointments from Firebase
        retrieveAppointments()

        // Logout button click listener
        logoutButton.setOnClickListener {
            val intent = Intent(this, LogoutPage::class.java)
            startActivity(intent)
            finish() // Close the Dashboard activity
        }
    }

    private fun retrieveAppointments() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointments.clear()
                for (appointmentSnapshot in snapshot.children) {
                    val appointment = appointmentSnapshot.getValue(Appointment::class.java)
                    if (appointment != null) {
                        appointments.add(appointment)
                    }
                }

                // Update ListView with appointments
                val adapter = AppointmentAdapter(this@Dashboard, appointments)
                appointmentListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Dashboard, "Failed to load appointments", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
