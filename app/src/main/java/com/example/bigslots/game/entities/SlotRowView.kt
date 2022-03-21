package com.example.bigslots.game.entities

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.bigslots.R
import com.example.bigslots.databinding.SlotRowLayoutBinding
import com.example.bigslots.game.EventEnd
import com.example.bigslots.game.nextIntArray
import com.example.bigslots.game.setSlotAsset
import kotlin.random.Random

class SlotRowView: FrameLayout {
    companion object{
        const val ANIMATION_TIME = 180
    }

    private lateinit var binding: SlotRowLayoutBinding

    private lateinit var slot1: Slot
    private lateinit var slot2: Slot
    private lateinit var slot3: Slot
    private lateinit var nextRow: FrameLayout
    private lateinit var currentRow: FrameLayout

    private var oldValue = 0
    private var currentValue = 0

    lateinit var viewSlots: MutableList<Slot>

    internal lateinit var eventEnd: EventEnd

    fun setEventEnd(eventEnd: EventEnd){
        this.eventEnd = eventEnd
    }

    constructor(context: Context): super(context){
        initialize(context)
    }

    constructor(context: Context, attr: AttributeSet): super(context){
        initialize(context)
    }

    private fun initialize(context: Context){
        val inflater = LayoutInflater.from(context)
        binding = SlotRowLayoutBinding.inflate(inflater, this, false)
        inflater.inflate(R.layout.slot_row_layout, this)

        //nagovnokodili
        findImages()

        binding.nextRow.translationY = height.toFloat()
    }

    fun eventStart(roll: Int, images: IntArray){

        val randomImages = Random.nextIntArray(3, 7)

        //Log.d("animation_params", "roll: $roll")
        Log.d("animation_params", "images: ${images.joinToString()}")
        setNextValues(slot1, randomImages[0])
        setNextValues(slot2, randomImages[1])
        setNextValues(slot3, randomImages[2])

        currentRow.translationY = 0f
        currentRow.animate()
            .translationY(height.toFloat())
            .setDuration(ANIMATION_TIME.toLong())
            .start()

        nextRow.translationY = - nextRow.height.toFloat()
        nextRow.animate()
            .translationY(0f)
            .setDuration(ANIMATION_TIME.toLong())
            .setListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    nextRow.translationY = 0f
                    if(oldValue != roll){
                        eventStart(roll, images)
                        oldValue++
                        //Log.d("animation_params", "val $oldValue")
                    }else{
                        currentValue = 0
                        oldValue = 0
                        setNextValues(slot1, images[0])
                        setNextValues(slot2, images[1])
                        setNextValues(slot3, images[2])
                        //Toast.makeText(context, "ANIMATION_FINISHED", Toast.LENGTH_SHORT).show()
                        /*currentRow.forEachIndexed { index ,slot ->
                            (slot as ImageView).setSlotAsset(images[index])
                        }*/
                        eventEnd.eventEnd()
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            }).start()
}

    private fun setCurrentValues(slot: Slot, value: Int){
        slot.currentSlot.setSlotAsset(value)
        slot.currentSlot.tag = value
    }

    private fun setNextValues(slot: Slot, value: Int){
        slot.nextSlot.setSlotAsset(value)
        slot.nextSlot.tag = value
    }

    //govnokodim
    fun findImages(){
        slot1 = Slot(findViewById(R.id.currentImageOne), findViewById(R.id.nextImageOne))
        slot2 = Slot(findViewById(R.id.currentImageTwo), findViewById(R.id.nextImageTwo))
        slot3 = Slot(findViewById(R.id.currentImageThree), findViewById(R.id.nextImageThree))
        viewSlots = mutableListOf(slot1, slot2, slot3)
        currentRow = findViewById(R.id.current_row)
        nextRow = findViewById(R.id.next_row)
    }

}