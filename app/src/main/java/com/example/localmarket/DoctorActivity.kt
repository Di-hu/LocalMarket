package com.example.localmarket

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText

class DoctorActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DoctorAdapter
    lateinit var doctorList: ArrayList<DoctorModel>
    lateinit var searchDoctor: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        recyclerView = findViewById(R.id.doctorRecycler)
        searchDoctor = findViewById(R.id.searchDoctor)

        recyclerView.layoutManager = LinearLayoutManager(this)

        doctorList = ArrayList()

        doctorList.add(
            DoctorModel(
                "Dr. Amit Sharma",
                "Cardiologist",
                "City Hospital",
                "9876543210",
                R.drawable.img_9,
                4.5f,
                listOf("10 AM","11 AM","12 PM")
            )
        )

        doctorList.add(
            DoctorModel(
                "Dr. Neha Kapoor",
                "Gynecologist",
                "Care Clinic",
                "9876512345",
                R.drawable.img_10,
                4.2f,
                listOf("2 PM","3 PM","4 PM")
            )
        )

        doctorList.add(
            DoctorModel(
                "Dr. Rahul Verma",
                "General Physician",
                "Health Center",
                "9876509876",
                R.drawable.img_11,
                4.0f,
                listOf("9 AM","10 AM","11 AM")
            )
        )

        doctorList.add(
            DoctorModel("Dr. Priya Mehta","Dermatologist","Skin Clinic","9876501111",
                R.drawable.img_12,4.4f,listOf("1 PM","2 PM","3 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Arjun Singh","Orthopedic","Bone Hospital","9876522222",
                R.drawable.img_13,4.3f,listOf("10 AM","12 PM","2 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Kavita Jain","Pediatrician","Child Care Center","9876533333",
                R.drawable.img_14,4.6f,listOf("9 AM","11 AM","1 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Rohan Patel","Neurologist","Neuro Clinic","9876544444",
                R.drawable.img_15,4.1f,listOf("3 PM","4 PM","5 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Anjali Gupta","ENT Specialist","ENT Care","9876555555",
                R.drawable.img_16,4.0f,listOf("10 AM","12 PM","1 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Vikram Khanna","Dentist","Dental Clinic","9876566666",
                R.drawable.img_17,4.7f,listOf("11 AM","1 PM","3 PM"))
        )

        doctorList.add(
            DoctorModel("Dr. Sneha Roy","Psychiatrist","Mind Care","9876577777",
                R.drawable.img_18,4.5f,listOf("4 PM","5 PM","6 PM"))
        )

        adapter = DoctorAdapter(this, doctorList)
        recyclerView.adapter = adapter

        searchDoctor.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val filtered = doctorList.filter {
                    it.specialization.contains(s.toString(), true)
                }

                adapter.updateList(filtered)
            }
        })
    }
}