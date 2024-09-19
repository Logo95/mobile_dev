import kotlin.math.sin

fun main() {
    println("task 1")
    val firstNumber = 15.09F
    val secondNumber = 35.1F
    val eee: Double = (firstNumber + secondNumber).toDouble()
    println(eee)

    println("task 2")
    val numberOne = 6
    val numberTwo = 5
    val result = numberOne / numberTwo
    val remainder = numberOne % numberTwo
    println("При делении $numberOne на $numberTwo результат равен $result, остаток равен $remainder")
    println("Результат деления $numberOne на $numberTwo равен $result $remainder/$numberTwo")

    println("task 3")
    val monthOfBirth = 8
    val yearOfBirth = 2001
    val totalYears = 2024 - 1 - yearOfBirth
    val totalMonth = totalYears * 12 + 8
    val totalDays = totalMonth * 30 + 11
    val totalSeconds = totalDays * 24 * 3600
    println("$totalYears years, $totalMonth months, $totalDays days and $totalSeconds seconds have passed since my birth")
    if (monthOfBirth <= 3) {
        println("Я родился в первом квартале")
    } else if (monthOfBirth <= 6) {
        println("Я родился во втором квартале")
    } else if (monthOfBirth <= 9) {
        println("Яродился в третьем квартале")
    } else {
        println("Я родился в четвертом квартале")
    }

    println("task 4")
    val ss: Double = sin(1.0)
    println(String.format("%.3f", ss))
}