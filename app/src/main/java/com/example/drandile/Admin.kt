package com.example.drandile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class Admin : AppCompatActivity() {

    private lateinit var appointmentsListView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var appointmentsList: MutableList<Appointment>
    private lateinit var appointmentIds: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        appointmentsListView = findViewById(R.id.appointmentsListView)
        appointmentsList = mutableListOf()
        appointmentIds = mutableListOf()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("appointments")

        // Load appointments
        loadAppointments()

        // Item click listener for ListView
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
        val selectedAppointment = appointmentsList[position]

        val options = arrayOf("Edit Appointment", "Delete Appointment")
        AlertDialog.Builder(this)
            .setTitle("Manage Appointment")
            .setItems(options) { dialog: DialogInterface, which: Int ->
                when (which) {
                    0 -> {
                        // Edit appointment
                        val intent = Intent(this, EditAppointmentActivity::class.java)
                        intent.putExtra("appointmentId", appointmentIds[position])
                        startActivity(intent)
                    }
                    1 -> {
                        // Delete appointment
                        deleteAppointment(appointmentIds[position])
                    }
                }
            }
            .show()
    }

    private fun deleteAppointment(appointmentId: String) {
        database.child(appointmentId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete appointment", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
