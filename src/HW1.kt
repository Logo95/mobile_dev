import kotlin.math.sqrt

fun main() {

    println("task 1")
    val firstString = "I can"
    val secondString = "play"
    println("$firstString $secondString!")

    println("task 2")
    val myAge = 23
    val myAgeInTenYears = 33
    val daysInYear = 365.25
    val daysPassed: Float = (daysInYear * myAgeInTenYears).toFloat()
    println("Мой возраст $myAge лет. Через 10 лет, мне будет $myAgeInTenYears лет," +
            " с момента моего рождения пройдет $daysPassed дней. ")

    println("task 3")
    val leg1 = 12.0
    val leg2 = 7.0
    val square: Double = 0.5 * leg1 * leg2
    val perimeter: Double = leg1 + leg2 + sqrt(leg1 * leg1 + leg2 * leg2)
    println("Площадь треугольника равна $square, периметр равен $perimeter")
}