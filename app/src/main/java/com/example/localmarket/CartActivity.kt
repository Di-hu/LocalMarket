package com.example.localmarket

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.*

class CartActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var totalText: TextView

    private lateinit var houseInput: EditText
    private lateinit var streetInput: EditText
    private lateinit var landmarkInput: EditText
    private lateinit var cityInput: EditText
    private lateinit var pincodeInput: EditText

    private lateinit var paymentGroup: RadioGroup
    private lateinit var checkoutBtn: Button
    private lateinit var currentLocationBtn: Button
    private lateinit var mapPickerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // 🧾 CART LIST
        listView = findViewById(R.id.cartListView)
        totalText = findViewById(R.id.totalPrice)

        // 📍 ADDRESS INPUTS
        houseInput = findViewById(R.id.houseInput)
        streetInput = findViewById(R.id.streetInput)
        landmarkInput = findViewById(R.id.landmarkInput)
        cityInput = findViewById(R.id.cityInput)
        pincodeInput = findViewById(R.id.pincodeInput)

        // 💳 PAYMENT
        paymentGroup = findViewById(R.id.paymentGroup)
        checkoutBtn = findViewById(R.id.checkoutBtn)
        currentLocationBtn = findViewById(R.id.currentLocationBtn)
        mapPickerBtn = findViewById(R.id.mapPickerBtn)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 🧾 LOAD CART ITEMS
        loadCart()

        // 📍 CURRENT LOCATION
        currentLocationBtn.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                return@setOnClickListener
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {

                    val geocoder = Geocoder(this, Locale.getDefault())
                    val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (!list.isNullOrEmpty()) {
                        val address = list[0]

                        streetInput.setText(address.thoroughfare)
                        cityInput.setText(address.locality)
                        pincodeInput.setText(address.postalCode)
                        landmarkInput.setText(address.featureName)
                    }
                }
            }
        }

        // 🗺 MAP PICKER
        mapPickerBtn.setOnClickListener {
            val intent = Intent(this, MapPickerActivity::class.java)
            startActivityForResult(intent, 200)
        }

        // 💳 CHECKOUT
        checkoutBtn.setOnClickListener {

            val house = houseInput.text.toString().trim()
            val street = streetInput.text.toString().trim()
            val city = cityInput.text.toString().trim()
            val pincode = pincodeInput.text.toString().trim()

            if (house.isEmpty() || street.isEmpty() || city.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "Please fill complete address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (paymentGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cartItems = CartManager.getCartList()

            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ CREATE ITEMS STRING
            val itemsText = cartItems.joinToString(", ") {
                "${it.name} (x${it.quantity})"
            }

            val totalPrice = CartManager.getTotalPrice()
            val fullAddress = "$house, $street, $city - $pincode"

            val order = OrderModel(
                id = System.currentTimeMillis().toString(),
                items = itemsText,
                total = totalPrice.toString(),
                address = fullAddress,
                status = "Preparing",
                time = System.currentTimeMillis()
            )

            OrderManager.saveOrder(this, order)

            Toast.makeText(this, "Payment Successful 🎉", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this@CartActivity, TrackingActivity::class.java)
                intent.putExtra("orderId", order.id)
                startActivity(intent)

                CartManager.clearCart()
                finish()

            }, 1200)
        }
    }

    // 🧾 LOAD CART DATA (NO * USED)
    private fun loadCart() {

        val cartItems = CartManager.getCartList()

        val displayList = cartItems.map {

            val price = it.price.toString().replace("₹", "").toInt()
            val quantity = it.quantity

            var total = 0

            // ✅ multiply WITHOUT *
            repeat(quantity) {
                total += price
            }

            "${it.name} x$quantity = ₹$total"
        }

        listView.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)

        totalText.text = "Total: ₹${CartManager.getTotalPrice()}"
    }

    // 📥 MAP RESULT
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            val lat = data?.getDoubleExtra("lat", 0.0) ?: 0.0
            val lng = data?.getDoubleExtra("lng", 0.0) ?: 0.0

            val geocoder = Geocoder(this, Locale.getDefault())
            val list = geocoder.getFromLocation(lat, lng, 1)

            if (!list.isNullOrEmpty()) {
                val address = list[0]

                streetInput.setText(address.thoroughfare)
                cityInput.setText(address.locality)
                pincodeInput.setText(address.postalCode)
                landmarkInput.setText(address.featureName)
            }
        }
    }
}