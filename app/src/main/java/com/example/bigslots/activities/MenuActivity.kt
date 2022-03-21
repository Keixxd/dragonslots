package com.example.bigslots.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.bigslots.databinding.ActivityMenuBinding
import com.example.bigslots.game.data.PlayerData

class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerData.score = PlayerData.loadScore(this,this)
        onBindButtons()
    }

    private fun onBindButtons() {
        with(binding) {
            play.setOnClickListener {
                startActivity(Intent(this@MenuActivity, MainActivity::class.java))
            }
            getCoins.setOnClickListener {
                startActivity(Intent(this@MenuActivity, CoinsActivity::class.java))
            }
            privacy.setOnClickListener {
                openPrivacyTab()
            }
        }
    }

    private fun openPrivacyTab(){
        val url = "https://www.google.com"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun onStop() {
        super.onStop()
        PlayerData.saveScore(this,this)
    }
}