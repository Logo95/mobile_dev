//Задание 2
fun sumOfNumbers(vararg numbers: Int): Int {
    var sum = 0
    for (number in numbers) {
        sum += number
    }
    return sum
}

//3.1
fun isEven(number: Int): Boolean {
    return number % 2 == 0
}

//3.2
fun isDivisibleByThree(number: Int): Boolean {
    return number % 3 == 0
}

//3.3
fun createArrayInRange(x: Int, y: Int): List<Int> {
    return (x..y).toList()
}

//3.5
fun filterOutEvenNumbers(numbers: List<Int>): List<Int> {
    return numbers.filter { !isEven(it) }
}

//3.6
fun filterOutDivisibleByThree(numbers: List<Int>): List<Int> {
    return numbers.filter { !isDivisibleByThree(it) }
}


fun main(){
    //Задание 1
    val gameResults = mapOf(
        "Салават Юлаев" to listOf("3:6", "5:5", "N/A"),
        "Авангард" to listOf("2:1"),
        "АкБарс" to listOf("3:3", "1:2")
    )

    for ((team, results) in gameResults) {
        for (result in results) {
            println("Игра с $team - $result")
        }
    }

    //Задание 2
    val result1 = sumOfNumbers(5, 10, 15, 20)
    println("Сумма чисел: $result1")

    val numberArray = intArrayOf(3, 6, 9, 12)
    val result2 = sumOfNumbers(*numberArray)
    println("Сумма чисел из массива: $result2")

    //3.4
    val array1To100 = createArrayInRange(1, 100)
    println("Массив чисел от 1 до 100: $array1To100")

    //3.7
    val array1To100_2 = createArrayInRange(1, 100)

    val noEvenNumbers = filterOutEvenNumbers(array1To100_2)
    println("Массив без четных чисел: $noEvenNumbers")

    val noDivisibleByThree = filterOutDivisibleByThree(array1To100_2)
    println("Массив без чисел, кратных 3: $noDivisibleByThree")

    val filteredArray = filterOutEvenNumbers(filterOutDivisibleByThree(array1To100_2))
    println("Массив без четных чисел и без чисел, кратных 3: $filteredArray")
}