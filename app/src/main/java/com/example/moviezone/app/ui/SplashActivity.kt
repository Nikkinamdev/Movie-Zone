package com.example.moviezone.app.ui

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviezone.MainActivity
import com.example.moviezone.R

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var isNevigated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        val session = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({

            if (session.isLoggedIn()) {

                startActivity(Intent(this, HomeActivity::class.java))

            } else {
                NevigateToOnboard()
            }

            finish()

        }, 1500) // s

    }

    private fun NevigateToOnboard() {
        handler.removeCallbacksAndMessages(null)
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}