package com.example.localmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(private val list: List<Medicine>) :
    RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.medName)
        val price: TextView = view.findViewById(R.id.medPrice)
        val desc: TextView = view.findViewById(R.id.medDesc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val medicine = list[position]

        holder.name.text = medicine.name
        holder.price.text = "₹ ${medicine.price}"
        holder.desc.text = medicine.description
    }
}