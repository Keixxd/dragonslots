package com.example.bigslots.activities

import android.animation.Animator
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.forEach
import com.example.bigslots.game.EventEnd
import com.example.bigslots.R
import com.example.bigslots.databinding.ActivityMainBinding
import com.example.bigslots.game.*
import com.example.bigslots.game.data.EventResults
import com.example.bigslots.game.data.PlayerData
import com.example.bigslots.game.data.ResultsActions
import com.example.bigslots.game.entities.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), EventEnd {

    private lateinit var binding: ActivityMainBinding
    private val playScreen = mutableListOf<SlotRowView>()
    private var countDown = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggleFullscreen()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.playScreen.forEach { row ->
            playScreen.add(row as SlotRowView)
        }

        playScreen.forEach {
            it.setEventEnd(this@MainActivity)
        }

        onBindBetButtons()
        updateScore()

        binding.spinButton.setOnClickListener {
            binding.spinButton.isClickable = false
            hideLines()
            PlayerData.win = 0
            hideResultMessage()
            if(PlayerData.score >= PlayerData.bet) {
                PlayerData.score -= PlayerData.bet
                updateScore()
                playScreen.forEach { row ->
                    row.eventStart(
                        images = Random.nextIntArray(3, 7),
                        roll = Random.nextInt(6) + 1
                    )
                }
            }else{
                showResultMessage("Not enough tokens..")
                binding.spinButton.isClickable = true
            }
        }

    }

    private fun hideLines() {
        binding.redLine.visibility = View.INVISIBLE
        binding.greenLine.visibility = View.INVISIBLE
        binding.blueLine.visibility = View.INVISIBLE
    }

    private fun onBindBetButtons(){

        binding.betTv.text = PlayerData.bet.toString()

        binding.plusBet.setOnClickListener {
            if(PlayerData.bet < 250){
                PlayerData.bet += 50
                binding.betTv.text = PlayerData.bet.toString()
            }
        }

        binding.minusBet.setOnClickListener {
            if(PlayerData.bet > 50){
                PlayerData.bet -= 50
                binding.betTv.text = PlayerData.bet.toString()
            }
        }

    }

    //slot value = next image tag

    override fun eventEnd() {
        if (countDown < 4) {
            countDown++
        } else {
            countDown = 0
            val resultMap = mutableMapOf<Int, List<Slot>>()
            playScreen.forEachIndexed { index, row ->
                resultMap.put(index, row.viewSlots)
            }
            val linesResults = mutableListOf<Line>()
            linesResults.add(Line(resultMap, PlaygroundLines.RED.id, binding))
            linesResults.add(Line(resultMap, PlaygroundLines.BLUE.id, binding))
            linesResults.add(Line(resultMap, PlaygroundLines.GREEN.id, binding))
            linesResults.forEach { line ->
                checkForEventResult(line)
            }

            onShowLines(linesResults)

            onDisplayResults()

            updateScore()

            Log.d("slot_event_results", "${PlayerData.win}")

            binding.spinButton.isClickable = true
            if(PlayerData.score == 0){
                ResultsActions().actionSpecial()
            }

        }
    }

    private fun onDisplayResults() {
        when {
            PlayerData.win == 0 -> showResultMessage(resources.getString(R.string.lose))
            PlayerData.win <= 200 -> showResultMessage(resources.getString(R.string.win_three))
            PlayerData.win <= 600 -> showResultMessage(resources.getString(R.string.win_two))
            PlayerData.win > 600 -> showResultMessage(resources.getString(R.string.win_one))

        }
    }

    private fun updateScore() {
        binding.creditTv.text = PlayerData.score.toString()
    }

    private fun checkForEventResult(line: Line) {
        when (line.winCondition) {
            EventResults.BIGWIN -> {
                ResultsActions().actionBigWin()
            }
            EventResults.MEDIUMWIN -> {
                ResultsActions().actionMediumWin()
            }
            EventResults.SMALLWIN -> {
                ResultsActions().actionSmallWin()
            }
            EventResults.LOSE -> {
                ResultsActions().actionLose()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toggleFullscreen()
    }

    @Suppress("DEPRECATION")
    private fun toggleFullscreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun onShowLines(lines: List<Line>){
        lines.forEach { line ->
            if(line.winCondition != EventResults.LOSE)
                line.onDrawLine()
        }
    }

    private fun showResultMessage(result: String) {
        binding.resultLayout.alpha = 0f
        binding.resultLayout.animate()
            .setDuration(300.toLong())
            .alpha(1f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    binding.resultText.text = result
                    if (PlayerData.win != 0) {
                        binding.winText.text = "+ ${PlayerData.win}"
                    }
                }

                override fun onAnimationEnd(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {}
            })
            .start()

    }

    private fun hideResultMessage() {

        binding.resultLayout.animate()
            .setDuration(300.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.winText.text = ""
                    binding.resultText.text = ""
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
            .alpha(0f)
            .start()
    }

    override fun onStop() {
        super.onStop()
        PlayerData.saveScore(this,this)
        Log.d("slot_event_results", "onStop")
    }
}