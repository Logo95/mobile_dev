import kotlin.random.Random

// 1.1
data class Employee(
    val salary: Int,
    val name: String,
    val surname: String
)

fun main() {
    // 1.2
    val names = arrayOf("John", "Aaron", "Tim", "Ted", "Steven")
    val surnames = arrayOf("Smith", "Dow", "Isaacson", "Pennyworth", "Jankins")

    // 1.3
    val employees = mutableListOf<Employee>()

    for (i in 1..10) {
        val name = names.random()
        val surname = surnames.random()
        val salary = Random.nextInt(1000, 2001) // Генерация зарплаты от $1000 до $2000
        employees.add(Employee(salary, name, surname))
    }

    // 1.4
    for (employee in employees) {
        println("${employee.name} ${employee.surname}'s salary is $${employee.salary}")
    }

    // 1.5
    val evenSalaryEmployees = employees.filter { it.salary % 2 == 0 }

    println("\nEmployees with even salaries:")
    for (employee in evenSalaryEmployees) {
        println("${employee.name} ${employee.surname}'s salary is $${employee.salary}")
    }
}
