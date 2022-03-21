package com.example.bigslots.game.entities

import android.widget.ImageView

data class Slot(
    var currentSlot: ImageView,
    var nextSlot: ImageView,
    var result: Int = 0
)
