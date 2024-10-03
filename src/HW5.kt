import kotlin.random.Random

fun main() {
    // Задание 1
    val deposit = 500_000.0
    val rate = 0.11
    val period = 5
    var totalDeposit = deposit

    for (year in 1..period) {
        totalDeposit += totalDeposit * rate
    }
    val profit = totalDeposit - deposit
    println("Сумма вклада через $period лет увеличится на ${"%.2f".format(profit)} и составит ${"%.2f".format(totalDeposit)} рублей")

    // Задание 2
    val numbers = arrayOf(3, 7, 12, 19, 22, 9, 44, 31)
    println("Четные числа:")
    for (number in numbers) {
        if (number % 2 != 0) continue
        println(number)
    }

    println("Нечетные числа:")
    for (number in numbers) {
        if (number % 2 == 0) continue
        println(number)
    }

    // Задание 3
    var iteration = 0
    while (true) {
        iteration++
        val randomNumber = Random.nextInt(1, 11)
        if (randomNumber == 5) {
            println("Чтобы выпало число 5 понадобилось $iteration итераций.")
            break
        }
    }

    // Задание 4
    val height = 10
    var currentHeight = 0
    var days = 0

    while (currentHeight < height) {
        days++
        currentHeight += 2
        if (currentHeight >= height) break
        currentHeight -= 1
    }

    println("Черепашка заберется на столб через $days дней.")
}