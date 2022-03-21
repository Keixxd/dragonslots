package com.example.bigslots.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.bigslots.R

class StartActivity: AppCompatActivity() {

    private val PRELOAD_ACTIVITY_LENGTH: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }, PRELOAD_ACTIVITY_LENGTH)
    }
}