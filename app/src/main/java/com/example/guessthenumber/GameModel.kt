package com.example.guessthenumber

import kotlin.random.Random

class GameModel {
    private val maxRounds = 5
    var currentRound = 1
    var targetNumber = 0
    var totalScore = 0

    // Инициализация нового раунда
    fun startNewRound() {
        if (currentRound <= maxRounds) {
            targetNumber = Random.nextInt(1, 51) // Случайное число от 1 до 50
        }
    }

    // Подсчёт очков за раунд
    fun calculateScore(guess: Int): Int {
        val difference = kotlin.math.abs(targetNumber - guess)
        val roundScore = 50 - difference // Чем ближе, тем больше очков
        totalScore += roundScore
        return roundScore
    }

    // Проверка завершения игры
    fun isGameOver(): Boolean {
        return currentRound > maxRounds
    }
}
