package com.example.drandile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class BookAppointment : AppCompatActivity() {

    private lateinit var patientNameInput: EditText
    private lateinit var contactNumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var doctorSpinner: Spinner
    private lateinit var datePickerButton: Button
    private lateinit var dateTextView: TextView // To display selected date
    private lateinit var timeSpinner: Spinner
    private lateinit var appointmentNotesInput: EditText
    private lateinit var confirmBookingButton: Button

    private lateinit var database: DatabaseReference
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge layout
        setContentView(R.layout.activity_book_appointment)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("appointments")

        // Initialize UI components
        patientNameInput = findViewById(R.id.patientNameInput)
        contactNumberInput = findViewById(R.id.contactNumberInput)
        emailInput = findViewById(R.id.emailInput)
        doctorSpinner = findViewById(R.id.doctorSpinner)
        datePickerButton = findViewById(R.id.datePickerButton)
        dateTextView = findViewById(R.id.selectDateText) // TextView to display selected date
        timeSpinner = findViewById(R.id.timeSpinner)
        appointmentNotesInput = findViewById(R.id.appointmentNotesInput)
        confirmBookingButton = findViewById(R.id.confirmBookingButton)

        // Set up the spinner for doctors (example values)
        val doctors = arrayOf("Dr. Andile", "Dr. Smith", "Dr. Jane")
        val doctorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, doctors)
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctorSpinner.adapter = doctorAdapter

        // Set up the spinner for available appointment times (example values)
        val times = arrayOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM")
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter

        // Date Picker Button Click Listener
        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        // Confirm Booking Button Click Listener
        confirmBookingButton.setOnClickListener {
            saveAppointment {
                startActivity(Intent(this, Dashboard::class.java))
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            dateTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveAppointment(onComplete: () -> Unit) {
        val name = patientNameInput.text.toString()
        val contact = contactNumberInput.text.toString()
        val email = emailInput.text.toString()
        val doctor = doctorSpinner.selectedItem.toString()
        val time = timeSpinner.selectedItem.toString()
        val notes = appointmentNotesInput.text.toString()

        // Basic input validation
        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || selectedDate.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a unique key for the appointment
        val appointmentId = database.push().key ?: return

        val appointment = Appointment(appointmentId, name, contact, email, doctor, selectedDate, time, notes)

        database.child(appointmentId).setValue(appointment).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show()
                onComplete()
            } else {
                // Log the error message
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Toast.makeText(this, "Failed to book appointment: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

