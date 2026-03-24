package com.example.localmarket

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    private val UPI_PAYMENT = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val doctorInfo = findViewById<TextView>(R.id.appointmentInfo)

        val doctorName = intent.getStringExtra("doctorName")
        val timeSlot = intent.getStringExtra("timeSlot")

        doctorInfo.text = "Doctor: $doctorName\nTime: $timeSlot\nFee: ₹500"

        val gpay = findViewById<LinearLayout>(R.id.gpayLayout)
        val paytm = findViewById<LinearLayout>(R.id.paytmLayout)
        val phonepe = findViewById<LinearLayout>(R.id.phonepeLayout)

        gpay.setOnClickListener { openUPI("com.google.android.apps.nbu.paisa.user") }
        paytm.setOnClickListener { openUPI("net.one97.paytm") }
        phonepe.setOnClickListener { openUPI("com.phonepe.app") }
    }

    private fun openUPI(packageName: String) {

        val uri = Uri.parse(
            "upi://pay?pa=test@upi&pn=DoctorAppointment&tn=Consultation&am=500&cu=INR"
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(packageName)

        startActivityForResult(intent, UPI_PAYMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPI_PAYMENT) {

            Toast.makeText(
                this,
                "Payment Completed (Demo Mode)",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}