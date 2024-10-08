//ПРАКТИЧЕСКОЕ ЗАДАНИЕ №1

fun main() {
    // 1. Объявите массив, состоящий из 10 целочисленных значений
    val array = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    // 2. Убедитесь (при помощи метода), что элементов действительно 10
    println("Длина массива: ${array.size}") // Должно вывести 10

    // 3. Добавьте в ваш массив 3 новых элемента
    array.addAll(listOf(11, 12, 13))
    println("После добавления 3 элементов: $array")

    // 4. Удалите из вашего массива первый элемент
    array.removeAt(0)
    println("После удаления первого элемента: $array")

    // 5. Удалите из вашего массива пятый элемент
    array.removeAt(4)
    println("После удаления пятого элемента: $array")

    // 6. Добавьте на место пятого элемента значение 15
    array.add(4, 15)
    println("После добавления 15 на пятое место: $array")

    // 7. Удалите последний элемент из массива
    array.removeAt(array.size - 1)
    println("После удаления последнего элемента: $array")

    // 8. Удалите все элементы из массива
    array.clear()
    println("После удаления всех элементов: $array")
}
