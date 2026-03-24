package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)
        val appName = findViewById<TextView>(R.id.appName)


        logo.scaleX = 0.7f
        logo.scaleY = 0.7f
        logo.alpha = 0f


        logo.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()


        appName.alpha = 0f
        appName.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(700)
            .start()


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)
    }
}