package com.example.drandile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AppointmentAdapter(context: Context, private val appointments: List<Appointment>) :
    ArrayAdapter<Appointment>(context, 0, appointments) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val appointment = appointments[position]

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        // Lookup view for data population
        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        // Populate the data into the template view using the data object
        text1.text = appointment.patientName
        text2.text = "${appointment.date} at ${appointment.time} with ${appointment.doctor}"

        // Return the completed view to render on screen
        return view
    }
}
