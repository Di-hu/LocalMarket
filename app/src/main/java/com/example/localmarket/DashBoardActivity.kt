package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashBoardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        auth = FirebaseAuth.getInstance()

        val greeting = findViewById<TextView>(R.id.tvGreeting)

        val medicinesCard = findViewById<LinearLayout>(R.id.cardMedicines)
        val doctorCard = findViewById<LinearLayout>(R.id.cardDoctor)
        val pharmacyCard = findViewById<LinearLayout>(R.id.cardPharmacy)
        val healthCard = findViewById<LinearLayout>(R.id.cardHealth)

        val searchBar = findViewById<EditText>(R.id.searchEt)




        // Show user name
        val user = auth.currentUser
        if (user != null) {

            val name = user.email!!.substringBefore("@")
            greeting.text = "Welcome $name 👋"

        }



        medicinesCard.setOnClickListener {

            startActivity(Intent(this, MedicinesActivity::class.java))

        }


        // Doctor Card
        doctorCard.setOnClickListener {

            startActivity(Intent(this, DoctorActivity::class.java))

        }



        pharmacyCard.setOnClickListener {

            startActivity(Intent(this, PharmacyActivity::class.java))

        }



        healthCard.setOnClickListener {

            startActivity(Intent(this, HealthTipsActivity::class.java))

        }


        searchBar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString()

                if (query.length >= 3) {

                    val intent = Intent(this@DashBoardActivity, MedicinesActivity::class.java)
                    intent.putExtra("searchQuery", query)
                    startActivity(intent)

                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })











    }
}