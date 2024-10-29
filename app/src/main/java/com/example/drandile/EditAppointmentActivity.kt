package com.example.drandile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditAppointmentActivity : AppCompatActivity() {

    private lateinit var patientNameInput: EditText
    private lateinit var contactNumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var doctorSpinner: Spinner
    private lateinit var dateTextView: TextView
    private lateinit var timeSpinner: Spinner
    private lateinit var appointmentNotesInput: EditText
    private lateinit var saveChangesButton: Button

    private lateinit var database: DatabaseReference
    private lateinit var appointmentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_appointment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("appointments")

        // Retrieve the appointment ID from the intent
        appointmentId = intent.getStringExtra("appointmentId") ?: ""

        // Initialize UI components
        patientNameInput = findViewById(R.id.patientNameInput)
        contactNumberInput = findViewById(R.id.contactNumberInput)
        emailInput = findViewById(R.id.emailInput)
        doctorSpinner = findViewById(R.id.doctorSpinner)
        dateTextView = findViewById(R.id.selectDateText)
        timeSpinner = findViewById(R.id.timeSpinner)
        appointmentNotesInput = findViewById(R.id.appointmentNotesInput)
        saveChangesButton = findViewById(R.id.saveChangesButton)

        // Set up adapters for the Spinners
        setUpDoctorSpinner()
        setUpTimeSpinner()

        // Set up date picker on dateTextView
        dateTextView.setOnClickListener {
            showDatePicker()
        }

        // Load the existing appointment details
        loadAppointmentDetails()

        // Save changes Button Click Listener
        saveChangesButton.setOnClickListener {
            if (validateInputs()) {
                updateAppointment()
            }
        }
    }

    // Set up Doctor Spinner with an adapter
    private fun setUpDoctorSpinner() {
        val doctors = arrayOf("Dr. Andile", "Dr. Smith", "Dr. Jane")
        val doctorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, doctors)
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctorSpinner.adapter = doctorAdapter
    }

    // Set up Time Spinner with an adapter
    private fun setUpTimeSpinner() {
        val times = arrayOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM")
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter
    }

    // Validate input fields before saving
    private fun validateInputs(): Boolean {
        if (patientNameInput.text.isNullOrEmpty() ||
            contactNumberInput.text.isNullOrEmpty() ||
            emailInput.text.isNullOrEmpty() ||
            doctorSpinner.selectedItem == null ||
            dateTextView.text.isNullOrEmpty() ||
            timeSpinner.selectedItem == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loadAppointmentDetails() {
        // Load the appointment details from Firebase using appointmentId
        database.child(appointmentId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val appointment = snapshot.getValue(Appointment::class.java)
                appointment?.let {
                    patientNameInput.setText(it.patientName)
                    contactNumberInput.setText(it.contactNumber)
                    emailInput.setText(it.email)

                    // Load doctor and time into Spinners after adapters are set
                    doctorSpinner.setSelection(getDoctorIndex(it.doctor))
                    timeSpinner.setSelection(getTimeIndex(it.time))

                    dateTextView.text = it.date
                    appointmentNotesInput.setText(it.notes)
                }
            } else {
                Toast.makeText(this, "Appointment not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load appointment", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAppointment() {
        // Collect data and update the appointment
        val name = patientNameInput.text.toString()
        val contact = contactNumberInput.text.toString()
        val email = emailInput.text.toString()
        val doctor = doctorSpinner.selectedItem.toString()
        val date = dateTextView.text.toString()
        val time = timeSpinner.selectedItem.toString()
        val notes = appointmentNotesInput.text.toString()

        val updatedAppointment = Appointment(appointmentId, name, contact, email, doctor, date, time, notes)

        // Update the appointment in Firebase
        database.child(appointmentId).setValue(updatedAppointment).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Appointment updated successfully", Toast.LENGTH_SHORT).show()

                // Start DashboardActivity and clear EditAppointmentActivity from the back stack
                val intent = Intent(this, Dashboard::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                finish() // Close the activity
            } else {
                Toast.makeText(this, "Failed to update appointment", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            Log.e("EditAppointment", "Error updating appointment", it)
        }
    }

    // Date picker dialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
            dateTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun getDoctorIndex(doctor: String): Int {
        val doctors = arrayOf("Dr. Andile", "Dr. Smith", "Dr. Jane")
        return doctors.indexOf(doctor)
    }

    private fun getTimeIndex(time: String): Int {
        val times = arrayOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM")
        return times.indexOf(time)
    }
}
