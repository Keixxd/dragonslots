package com.example.bigslots.game.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.bigslots.R

object PlayerData {

    var score = 1000
    var bet = 100
    var win = 0

    fun <T: AppCompatActivity> saveScore(context: Context, activity: T){
        val pref = activity.getSharedPreferences(context.getString(R.string.player_data_pref),Context.MODE_PRIVATE)
        with(pref.edit()){
            putInt(context.getString(R.string.player_score), score)
            apply()
        }
    }

    fun <T: AppCompatActivity> loadScore(context: Context, activity: T): Int {
        val pref = activity.getSharedPreferences(context.getString(R.string.player_data_pref), Context.MODE_PRIVATE)
        return pref.getInt(context.getString(R.string.player_score), 1000)
    }
}