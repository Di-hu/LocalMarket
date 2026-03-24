package com.example.localmarket

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MedicinesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedicineAdapter

    private val medicineList = mutableListOf<Medicine>()
    private val filteredList = mutableListOf<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines)

        recyclerView = findViewById(R.id.medicineRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MedicineAdapter(filteredList)
        recyclerView.adapter = adapter

        val query = intent.getStringExtra("searchQuery")

        loadMedicines(query)
    }


    private fun loadMedicines(query:String?){

        val db = FirebaseFirestore.getInstance()

        db.collection("Medicines")
            .get()
            .addOnSuccessListener { result ->

                medicineList.clear()
                filteredList.clear()

                for(doc in result){

                    val medicine = doc.toObject(Medicine::class.java)
                    medicineList.add(medicine)

                }

                if(query != null){

                    for(med in medicineList){

                        if(med.name.contains(query,true)){
                            filteredList.add(med)
                        }

                    }

                } else {

                    filteredList.addAll(medicineList)

                }

                adapter.notifyDataSetChanged()

                if(filteredList.isEmpty()){

                    Toast.makeText(this,"Medicine not found",Toast.LENGTH_SHORT).show()

                }

            }

            .addOnFailureListener {

                Toast.makeText(this,"Error loading medicines",Toast.LENGTH_SHORT).show()

            }

    }
}