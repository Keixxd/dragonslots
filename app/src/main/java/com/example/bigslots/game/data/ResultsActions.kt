package com.example.bigslots.game.data

import android.util.Log

class ResultsActions () {

    fun actionBigWin(){
        val result = PlayerData.bet *10
        PlayerData.score += result
        PlayerData.win += result
        Log.d("slot_event_results", "big win")
    }

    fun actionMediumWin(){
        val result = PlayerData.bet *5
        PlayerData.score += result
        PlayerData.win += result
        Log.d("slot_event_results", "medium win")
    }

    fun actionSmallWin(){
        val result = PlayerData.bet *2
        PlayerData.score += result
        PlayerData.win += result
        Log.d("slot_event_results", "small win")
    }

    fun actionLose(){
        Log.d("slot_event_results", "lose")
    }

    fun actionSpecial(){
        PlayerData.score += 500
    }


}