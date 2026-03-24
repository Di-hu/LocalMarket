package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MedicineMenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var medicineList: ArrayList<MedicineModel>
    private lateinit var viewCartBtn: Button   // ✅ Cart Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_menu)

        // Title
        val title = findViewById<TextView>(R.id.pharmacyTitle)
        val pharmacyName = intent.getStringExtra("pharmacyName")
        title.text = pharmacyName

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMedicine)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cart Button
        viewCartBtn = findViewById(R.id.viewCartBtn)
        viewCartBtn.visibility = View.GONE   // Initially hidden

        viewCartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Medicine List
        medicineList = ArrayList()

        medicineList.add(MedicineModel("Paracetamol", "₹50", "Fever and Pain Relief", R.drawable.img_33))
        medicineList.add(MedicineModel("Dolo 650", "₹30", "Body Pain and Fever", R.drawable.img_34))
        medicineList.add(MedicineModel("Crocin", "₹25", "Cold & Fever", R.drawable.img_35))
        medicineList.add(MedicineModel("Vitamin C", "₹120", "Immunity Booster", R.drawable.img_36))
        medicineList.add(MedicineModel("Ibuprofen", "₹80", "Inflammation & Pain", R.drawable.img_37))
        medicineList.add(MedicineModel("Amoxicillin", "₹100", "Antibiotic", R.drawable.img_38))
        medicineList.add(MedicineModel("Cough Syrup", "₹120", "Cough & cold relief", R.drawable.img_39))
        medicineList.add(MedicineModel("Omeprazole", "₹60", "Acid Reflux", R.drawable.img_40))
        medicineList.add(MedicineModel("Aspirin", "₹40", "Pain Relief", R.drawable.img_41))
        medicineList.add(MedicineModel("Azithromycin", "₹150", "Antibiotic for Infections", R.drawable.img_41))
        medicineList.add(MedicineModel("Pantoprazole", "₹90", "Acid Reflux and GERD", R.drawable.img_42))
        medicineList.add(MedicineModel("Cetirizine", "₹40", "Allergy and Sneezing Relief", R.drawable.img_43))
        medicineList.add(MedicineModel("Metformin", "₹110", "Controls Blood Sugar", R.drawable.img_44))
        medicineList.add(MedicineModel("ORS", "₹20", "Prevents Dehydration", R.drawable.img_45))
        medicineList.add(MedicineModel("Loperamide", "₹35", "Stops Loose Motion", R.drawable.img_46))
        medicineList.add(MedicineModel("Domperidone", "₹70", "Relieves Nausea and Vomiting", R.drawable.img_47))
        medicineList.add(MedicineModel("Salbutamol", "₹130", "Asthma and Breathing Relief", R.drawable.img_48))

        val adapter = MedicineMenuAdapter(this, medicineList)
        recyclerView.adapter = adapter
    }

    // 🔥 AUTO UPDATE CART BUTTON
    override fun onResume() {
        super.onResume()

        val totalItems = CartManager.getTotalItems()

        if (totalItems > 0) {
            viewCartBtn.visibility = View.VISIBLE
            viewCartBtn.text = "View Cart (" + totalItems + " items)"
        } else {
            viewCartBtn.visibility = View.GONE
        }
    }
}