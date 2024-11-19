package com.example.drandile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Admin : AppCompatActivity() {

    private lateinit var appointmentsListView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var appointmentsList: MutableList<Appointment>
    private lateinit var appointmentIds: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verify admin role
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(uid)
            userDatabase.child("role").get().addOnSuccessListener { snapshot ->
                val role = snapshot.getValue(String::class.java)
                if (role == "admin") {
                    setContentView(R.layout.activity_admin)
                    initializeAdminPage()
                } else {
                    Toast.makeText(this, "Access denied: Admins only", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to verify user role", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Please sign in to access admin features", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializeAdminPage() {
        appointmentsListView = findViewById(R.id.appointmentsListView)
        appointmentsList = mutableListOf()
        appointmentIds = mutableListOf()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("appointments")
        loadAppointments()

        appointmentsListView.setOnItemClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
        }
    }

    private fun loadAppointments() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appointmentsList.clear()
                appointmentIds.clear()
                for (snapshot in dataSnapshot.children) {
                    val appointment = snapshot.getValue(Appointment::class.java)
                    appointment?.let {
                        appointmentsList.add(it)
                        appointmentIds.add(snapshot.key ?: "")
                    }
                }
                val adapter = ArrayAdapter(this@Admin, android.R.layout.simple_list_item_1, appointmentsList.map { it.patientName })
                appointmentsListView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Failed to load appointments: ${databaseError.message}", databaseError.toException())
                Toast.makeText(this@Admin, "Failed to load appointments: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditDeleteDialog(position: Int) {
        val options = arrayOf("Edit Appointment", "Delete Appointment")
        AlertDialog.Builder(this)
            .setTitle("Manage Appointment")
            .setItems(options) { dialog: DialogInterface, which: Int ->
                when (which) {
                    0 -> editAppointment(position)
                    1 -> deleteAppointment(position)
                }
            }
            .show()
    }

    private fun editAppointment(position: Int) {
        val intent = Intent(this, EditAppointmentActivity::class.java)
        intent.putExtra("appointmentId", appointmentIds[position])
        startActivity(intent)
    }

    private fun deleteAppointment(position: Int) {
        val appointmentId = appointmentIds[position]
        database.child(appointmentId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete appointment", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
