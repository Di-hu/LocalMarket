package com.example.localmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PharmacyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pharmacyList: ArrayList<PharmacyModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy)


        recyclerView = findViewById(R.id.pharmacyRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pharmacyList = ArrayList()


        pharmacyList.add(PharmacyModel("Apollo Pharmacy", "Delhi", 4.5f, R.drawable.img_23))
        pharmacyList.add(PharmacyModel("MedPlus", "Punjab", 4.0f, R.drawable.img_24))
        pharmacyList.add(PharmacyModel("Wellness Pharmacy", "Mumbai", 3.8f, R.drawable.img_25))
        pharmacyList.add(PharmacyModel("CarePlus Pharmacy", "Chandigarh", 4.2f, R.drawable.img_26))
        pharmacyList.add(PharmacyModel("HealthKart Pharmacy", "Bangalore", 4.6f, R.drawable.img_27))
        pharmacyList.add(PharmacyModel("NetMeds Store", "Hyderabad", 4.3f, R.drawable.img_28))
        pharmacyList.add(PharmacyModel("Guardian Pharmacy", "Kolkata", 3.9f, R.drawable.img_29))
        pharmacyList.add(PharmacyModel("TrustCare Pharmacy", "Jaipur", 4.1f, R.drawable.img_30))
        pharmacyList.add(PharmacyModel("LifeCare Pharmacy", "Ahmedabad", 4.4f, R.drawable.img_31))
        pharmacyList.add(PharmacyModel("MediQuick Pharmacy", "Pune", 3.7f, R.drawable.img_32))
        pharmacyList.add(PharmacyModel("CityMed Pharmacy", "Lucknow", 4.0f, R.drawable.img_23))
        pharmacyList.add(PharmacyModel("GreenCross Pharmacy", "Indore", 4.2f, R.drawable.img_24))

        val adapter = PharmacyAdapter(this, pharmacyList)
        recyclerView.adapter = adapter
    }
}