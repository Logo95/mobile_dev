fun main() {
    // Задание 1
    print("Введите количество товара: ")
    val quantity = readLine()?.toIntOrNull() ?: return

    val pricePerItem = if (quantity <= 9) {
        1000
    } else if (quantity in 10..19) {
        800
    } else {
        600
    }

    val totalPrice = quantity * pricePerItem
    println("Общая стоимость за $quantity единиц товара: $totalPrice рублей")

    // Задание 2
    print("Введите число: ")
    val number = readLine()?.toIntOrNull() ?: return

    if (number % 2 == 0) {
        println("Число $number является четным.")
    } else {
        println("Число $number является нечетным.")
    }

    // Задание 3

    print("Введите порядковый номер пальца (1-5): ")
    val fingerNumber = readLine()?.toIntOrNull() ?: return

    if (fingerNumber == 1) {
        println("Это Большой палец.")
    } else if (fingerNumber == 2) {
        println("Это Указательный палец.")
    } else if (fingerNumber == 3) {
        println("Это Средний палец.")
    } else if (fingerNumber == 4) {
        println("Это Безымянный палец.")
    } else if (fingerNumber == 5) {
        println("Это Мизинец.")
    } else {
        println("Некорректный номер пальца.")
    }
}
