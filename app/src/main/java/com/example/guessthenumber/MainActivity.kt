package com.example.guessthenumber

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameModel: GameModel
    private lateinit var targetNumberText: TextView
    private lateinit var slider: SeekBar
    private lateinit var confirmButton: Button
    private lateinit var scoreText: TextView
    private lateinit var roundText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация модели и интерфейса
        gameModel = GameModel()
        targetNumberText = findViewById(R.id.targetNumberText)
        slider = findViewById(R.id.slider)
        confirmButton = findViewById(R.id.confirmButton)
        scoreText = findViewById(R.id.scoreText)
        roundText = findViewById(R.id.roundText)

        startNewRound()

        confirmButton.setOnClickListener {
            val guess = slider.progress + 1 // Значение слайдера (от 1 до 50)
            val score = gameModel.calculateScore(guess)

            Toast.makeText(
                this,
                "Ваше предположение: $guess, Очки за раунд: $score",
                Toast.LENGTH_SHORT
            ).show()

            if (gameModel.isGameOver()) {
                Toast.makeText(
                    this,
                    "Игра завершена! Ваш итоговый счёт: ${gameModel.totalScore}",
                    Toast.LENGTH_LONG
                ).show()
                resetGame()
            } else {
                gameModel.currentRound++
                startNewRound()
            }
        }
    }

    private fun startNewRound() {
        gameModel.startNewRound()
        targetNumberText.text = "Число: ${gameModel.targetNumber}"
        slider.progress = 24 // Устанавливаем слайдер в середину
        roundText.text = "Раунд: ${gameModel.currentRound}/5"
        scoreText.text = "Счёт: ${gameModel.totalScore}"
    }

    private fun resetGame() {
        gameModel = GameModel()
        startNewRound()
    }
}
