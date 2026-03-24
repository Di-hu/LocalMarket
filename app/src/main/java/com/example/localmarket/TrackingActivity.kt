package com.example.localmarket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.*
import kotlin.math.*

class TrackingActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var statusText: TextView
    private lateinit var etaText: TextView
    private lateinit var addressMain: TextView

    private lateinit var bikeMarker: Marker
    private lateinit var packageMarker: Marker

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var start: GeoPoint
    private lateinit var end: GeoPoint
    private val routePoints = mutableListOf<GeoPoint>()

    private var progress = 0.0

    // ⏱ TOTAL TIME (CHANGE HERE)
    private val totalDuration = 25 * 60 * 1000L // 25 minutes
    private val frameDelay = 50L
    private val totalSteps = totalDuration / frameDelay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_tracking)

        map = findViewById(R.id.map)
        statusText = findViewById(R.id.statusText)
        etaText = findViewById(R.id.etaText)
        addressMain = findViewById(R.id.addressMain)

        val orderId = intent.getStringExtra("orderId") ?: ""
        val order = OrderManager.getOrders(this).find { it.id == orderId }

        addressMain.text = order?.address?.take(60) ?: "Unknown Location"

        val dropLocation = getLatLngFromAddress(order?.address ?: "")

        if (dropLocation != null) {
            setupMap(dropLocation)
        }
    }

    private fun getLatLngFromAddress(address: String): GeoPoint? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val list = geocoder.getFromLocationName(address, 1)

            if (!list.isNullOrEmpty()) {
                val loc = list[0]
                GeoPoint(loc.latitude, loc.longitude)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun setupMap(drop: GeoPoint) {

        map.setMultiTouchControls(true)
        map.controller.setZoom(16.0)
        map.controller.setCenter(drop)
        map.setPadding(0, 0, 0, 220)

        start = GeoPoint(drop.latitude - 0.01, drop.longitude - 0.01)
        end = drop

        createRealisticRoute()

        val polyline = Polyline()
        polyline.setPoints(routePoints)
        polyline.color = android.graphics.Color.parseColor("#00FFAA")
        polyline.width = 8f
        map.overlays.add(polyline)

        packageMarker = Marker(map)
        packageMarker.position = start
        packageMarker.icon = getSmallIcon(R.drawable.img_59, 50)
        map.overlays.add(packageMarker)

        bikeMarker = Marker(map)
        bikeMarker.position = start
        bikeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        bikeMarker.icon = getSmallIcon(R.drawable.img_62, 60)
        bikeMarker.isEnabled = false
        map.overlays.add(bikeMarker)

        startTracking()
    }

    // 🛣 Realistic route (zig-zag road style)
    private fun createRealisticRoute() {
        routePoints.clear()

        val steps = 200

        for (i in 0..steps) {
            val t = i / steps.toDouble()

            val lat = start.latitude + (end.latitude - start.latitude) * t
            val lon = start.longitude + (end.longitude - start.longitude) * t

            val curveLat = 0.0015 * sin(t * Math.PI * 2)
            val curveLon = 0.001 * cos(t * Math.PI)

            routePoints.add(GeoPoint(lat + curveLat, lon + curveLon))
        }
    }

    private fun getSmallIcon(resId: Int, size: Int): BitmapDrawable {
        val bitmap = BitmapFactory.decodeResource(resources, resId)
        val scaled = Bitmap.createScaledBitmap(bitmap, size, size, true)
        return BitmapDrawable(resources, scaled)
    }

    // 🧭 REAL ANGLE CALCULATION
    private fun calculateBearing(start: GeoPoint, end: GeoPoint): Float {
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLon = lon2 - lon1

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) -
                sin(lat1) * cos(lat2) * cos(dLon)

        val bearing = Math.toDegrees(atan2(y, x))
        return ((bearing + 360) % 360).toFloat()
    }

    private fun startTracking() {

        handler.post(object : Runnable {
            override fun run() {

                if (progress <= 1.0) {

                    val status = when {
                        progress < 0.25 -> "Preparing "
                        progress < 0.5 -> "Packed 📦"
                        progress < 0.75 -> "Out for delivery 🚚"
                        else -> "Arriving soon 🏠"
                    }

                    statusText.text = status

                    val eta = ((1 - progress) * 25).toInt()
                    etaText.text = "Arriving in $eta min"

                    if (progress >= 0.5) {

                        packageMarker.isEnabled = false
                        bikeMarker.isEnabled = true

                        val index = (progress * routePoints.size).toInt()
                            .coerceIn(0, routePoints.size - 2)

                        val current = routePoints[index]
                        val next = routePoints[index + 1]

                        bikeMarker.position = current

                        // 🧭 REAL ROTATION
                        bikeMarker.rotation = calculateBearing(current, next)

                        // 📍 CAMERA FOLLOW
                        map.controller.animateTo(current)
                    }

                    map.invalidate()

                    progress += (1.0 / totalSteps)

                    handler.postDelayed(this, frameDelay)

                } else {
                    statusText.text = "Delivered ✅"
                    etaText.text = "Order Delivered"
                    bikeMarker.isEnabled = false
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}