fun main(){
    //1. Вывод чисел от 1 до 40:
    for (i in 1..40) {
        println(i)
    }

    //2. Сумма чисел, кратных 2 и 4, от 1 до N
    print("Введите число N: ")
    val N = readLine()?.toIntOrNull() ?: return

    var sum = 0
    var i = 1

    while (i <= N) {
        if (i % 2 == 0 && i % 4 == 0) {
            sum += i
        }
        i++
    }

    println("Сумма чисел, кратных 2 и 4, от 1 до $N: $sum")

    //3. Поиск количества элементов, больших заданного числа, в массиве
    val array = arrayOf(10, 5, 23, 7, 30, 15, 8, 25)

    print("Введите число: ")
    val number = readLine()?.toIntOrNull() ?: return

    var count = 0

    for (element in array) {
        if (element > number) {
            count++
        }
    }

    println("Количество элементов, больших $number: $count")

    //4. Проверка на делимость 9

    print("Введите число: ")
    var number1 = readLine()?.toIntOrNull() ?: return

    if (number1 % 9 == 0) {
        println("Число $number1 делится на 9 без остатка.")
    } else {
        println("Число $number1 не делится на 9 без остатка.")
    }

    //5. Типы животных
    print("Введите номер типа животного (1 для 'Млекопитающее', 2 для 'Птица', 3 для 'Рыба', 4 для 'Рептилия', 5 для 'Земноводное'): ")
    val number2 = readLine()?.toIntOrNull() ?: return

    val animalType = when (number2) {
        1 -> "Млекопитающее"
        2 -> "Птица"
        3 -> "Рыба"
        4 -> "Рептилия"
        5 -> "Земноводное"
        else -> "Неизвестный тип животного"
    }

    println("Тип животного: $animalType")
}