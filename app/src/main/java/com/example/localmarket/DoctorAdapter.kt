package com.example.localmarket

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(
    private val context: Context,
    private var doctorList: List<DoctorModel>
) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.doctorImage)
        val name: TextView = view.findViewById(R.id.tvName)
        val specialization: TextView = view.findViewById(R.id.tvSpecialization)
        val location: TextView = view.findViewById(R.id.tvLocation)
        val rating: RatingBar = view.findViewById(R.id.ratingBar)
        val spinner: Spinner = view.findViewById(R.id.spinnerTime)
        val phone: TextView = view.findViewById(R.id.tvPhone)
        val bookBtn: Button = view.findViewById(R.id.btnBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.doctor_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val doctor = doctorList[position]

        holder.image.setImageResource(doctor.image)
        holder.name.text = doctor.name
        holder.specialization.text = doctor.specialization
        holder.location.text = doctor.location
        holder.rating.rating = doctor.rating

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            doctor.timeSlots
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = adapter

        // Call doctor
        holder.phone.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${doctor.phone}")
            context.startActivity(intent)
        }

        // Book appointment -> Open payment screen
        holder.bookBtn.setOnClickListener {

            val selectedTime = holder.spinner.selectedItem.toString()

            val intent = Intent(context, PaymentActivity::class.java)

            intent.putExtra("doctorName", doctor.name)
            intent.putExtra("timeSlot", selectedTime)
            intent.putExtra("fee", "500")

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = doctorList.size

    fun updateList(newList: List<DoctorModel>) {
        doctorList = newList
        notifyDataSetChanged()
    }
}