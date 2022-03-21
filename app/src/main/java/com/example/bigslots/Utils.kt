package com.example.bigslots.game

import android.widget.ImageView
import com.example.bigslots.R
import com.example.bigslots.game.data.Values
import kotlin.random.Random

fun ImageView.setSlotAsset(value: Int){
    when(value){
        Values.QUEEN_VALUE.id -> {this.setImageResource(R.drawable.q_slot)}
        Values.JACK_VALUE.id ->{this.setImageResource(R.drawable.j_slot)}
        Values.KING_VALUE.id -> {this.setImageResource(R.drawable.k_slot)}
        Values.ACE_VALUE.id -> {this.setImageResource(R.drawable.a_slot)}
        Values.BALANCE_VALUE.id -> {this.setImageResource(R.drawable.balance)}
        Values.COINS_VALUE.id -> {this.setImageResource(R.drawable.coins)}
        Values.WILD.id -> {this.setImageResource(R.drawable.wild_slot)}
    }
}

fun Random.nextIntArray(size: Int, until: Int): IntArray{
    var result = mutableListOf<Int>()
    for(i in 0 until size){
        result.add(this.nextInt(until))
    }
    return result.toIntArray()
}