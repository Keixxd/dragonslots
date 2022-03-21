package com.example.bigslots.activities

import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.bigslots.R
import com.example.bigslots.databinding.ActivityCoinsBinding
import com.example.bigslots.game.data.PlayerData

class CoinsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCoinsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateScore()
        bindClicker()

        bindBack()

    }

    private fun bindBack() {
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun bindClicker(){
        binding.coinFarm.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.coin_button))
            PlayerData.score++
            updateScore()
        }
    }

    private fun updateScore(){
        binding.creditTv.text = PlayerData.score.toString()
    }

    override fun onStop() {
        super.onStop()
        PlayerData.saveScore(this, this)
        Log.d("slot_event_results", "onStop")
    }
}
