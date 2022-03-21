package com.example.bigslots.game.entities

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.bigslots.databinding.ActivityMainBinding
import com.example.bigslots.game.data.EventResults
import com.example.bigslots.game.data.Values

class Line(playground: Map<Int, List<Slot>>, lineId: Int, binding: ActivityMainBinding) {

    private val line: List<ImageView> = createLine(playground, lineId)
    private val lineId = lineId
    private val lineView: View = findLineView(binding)

    val winCondition: EventResults = countCondition(line)

    fun onDrawLine(){
        lineView.visibility = View.VISIBLE
    }

    private fun findLineView(binding: ActivityMainBinding): View {
        return when(lineId){
            PlaygroundLines.RED.id -> binding.redLine
            PlaygroundLines.BLUE.id -> binding.blueLine
            PlaygroundLines.GREEN.id -> binding.greenLine
            else -> binding.redLine
        }
    }

    private fun createLine(map: Map<Int, List<Slot>>, lineId: Int): List<ImageView>{
        val line = mutableListOf<ImageView>()
        map.values.toList().forEach{
            line.add(it.get(lineId).nextSlot)
        }
        Log.d("slot_event_results", line.joinToString(separator = ",") { it.tag.toString() } )
        return line
    }

    private fun countCondition(line: List<ImageView>): EventResults {
        val values = Values.values().toList()
        var index = 0
        var winCondition = EventResults.LOSE
        while(index < values.size && winCondition == EventResults.LOSE){
            winCondition = when(line.count {slot -> slot.tag == values[index].id}){
                3 -> EventResults.SMALLWIN
                4 -> EventResults.MEDIUMWIN
                5 -> EventResults.BIGWIN
                else -> EventResults.LOSE
            }
            index++
        }
        return winCondition
    }
}