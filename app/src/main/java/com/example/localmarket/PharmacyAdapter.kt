package com.example.localmarket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PharmacyAdapter(
    private val context: Context,
    private val pharmacyList: List<PharmacyModel>
) : RecyclerView.Adapter<PharmacyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.pharmacyImage)
        val name: TextView = view.findViewById(R.id.pharmacyName)
        val location: TextView = view.findViewById(R.id.pharmacyLocation)
        val rating: RatingBar = view.findViewById(R.id.pharmacyRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pharmacy_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pharmacy = pharmacyList[position]

        holder.name.text = pharmacy.name
        holder.location.text = pharmacy.location
        holder.rating.rating = pharmacy.rating
        holder.image.setImageResource(pharmacy.image)

        // ✅ CLICK FIX
        holder.itemView.setOnClickListener {

            Toast.makeText(holder.itemView.context, "Opening ${pharmacy.name}", Toast.LENGTH_SHORT).show()

            val intent = Intent(holder.itemView.context, MedicineMenuActivity::class.java)
            intent.putExtra("pharmacyName", pharmacy.name)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return pharmacyList.size
    }
}