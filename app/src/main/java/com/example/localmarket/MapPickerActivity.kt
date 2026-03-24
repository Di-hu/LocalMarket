package com.example.localmarket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        findViewById<View>(R.id.confirmLocationBtn).setOnClickListener {
            selectedLatLng?.let {
                val intent = Intent()
                intent.putExtra("lat", it.latitude)
                intent.putExtra("lng", it.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(26.9124, 75.7873)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        googleMap.setOnMapClickListener { latLng ->
            selectedLatLng = latLng

            marker?.remove()
            marker = googleMap.addMarker(
                MarkerOptions().position(latLng).title("Selected Location")
            )
        }
    }
}