package com.example.drandile



data class Appointment(
    val appointmentId: String = "",
    val patientName: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val doctor: String = "",
    val date: String = "",
    val time: String = "",
    val notes: String = ""
)
