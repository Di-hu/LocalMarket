package com.example.localmarket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class MedicineMenuAdapter(
    private val context: Context,
    private val medicineList: List<MedicineModel>
) : RecyclerView.Adapter<MedicineMenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.medicineImage)
        val name: TextView = view.findViewById(R.id.medicineName)
        val price: TextView = view.findViewById(R.id.medicinePrice)
        val description: TextView = view.findViewById(R.id.medicineUse)

        val addBtn: Button = view.findViewById(R.id.addBtn)
        val quantityLayout: LinearLayout = view.findViewById(R.id.quantityLayout)
        val btnPlus: TextView = view.findViewById(R.id.btnPlus)
        val btnMinus: TextView = view.findViewById(R.id.btnMinus)
        val qtyText: TextView = view.findViewById(R.id.qtyText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.medicine_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val medicine = medicineList[position]

        holder.name.text = medicine.name
        holder.price.text = medicine.price
        holder.description.text = medicine.description
        holder.image.setImageResource(medicine.image)

        // ✅ FIXED (No error now)
        var quantity = CartManager.getQuantity(medicine.name)

        if (quantity > 0) {
            holder.addBtn.visibility = View.GONE
            holder.quantityLayout.visibility = View.VISIBLE
            holder.qtyText.text = quantity.toString()
        } else {
            holder.addBtn.visibility = View.VISIBLE
            holder.quantityLayout.visibility = View.GONE
        }

        // ADD
        holder.addBtn.setOnClickListener {
            CartManager.addItem(medicine)

            quantity = CartManager.getQuantity(medicine.name)

            holder.addBtn.visibility = View.GONE
            holder.quantityLayout.visibility = View.VISIBLE
            holder.qtyText.text = quantity.toString()

            Toast.makeText(context, "${medicine.name} added", Toast.LENGTH_SHORT).show()
        }

        // PLUS
        holder.btnPlus.setOnClickListener {
            CartManager.addItem(medicine)
            quantity = CartManager.getQuantity(medicine.name)
            holder.qtyText.text = quantity.toString()
        }

        // MINUS
        holder.btnMinus.setOnClickListener {
            CartManager.removeItem(medicine)
            quantity = CartManager.getQuantity(medicine.name)

            if (quantity > 0) {
                holder.qtyText.text = quantity.toString()
            } else {
                holder.quantityLayout.visibility = View.GONE
                holder.addBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = medicineList.size
}