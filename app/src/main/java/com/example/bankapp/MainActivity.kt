package com.example.bankapp

import com.example.bankapp.controller.CashbackController
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.clickable
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankApp()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun BankApp() {
    val navController = rememberNavController()
    val balance = remember { mutableStateOf(10000.0) } // Глобальный баланс
    val transactions = remember { mutableStateOf(mutableListOf<Pair<String, Double>>()) } // Глобальный список транзакций (название партнера + процент)

    // Список партнеров с процентами кэшбэка
    val partners = listOf(
        Pair("BurgerKing", 3.0),
        Pair("Eldorado", 1.5),
        Pair("McDonalds", 2.0)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Банковское приложение") }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController) // Добавляем BottomNavigationBar
        }
    ) { innerPadding ->
        // Содержимое основной страницы с учётом padding от нижней навигации
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            balance = balance, // Передаем balance
            transactions = transactions, // Передаем transactions
            partners = partners // Передаем список partners
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Главная", "home", Icons.Default.Home),
        BottomNavItem("Счета", "balance", Icons.Default.AccountBalanceWallet),
        BottomNavItem("Переводы", "transfer", Icons.Default.Send),
        BottomNavItem("Настройки", "settings", Icons.Default.Settings)
    )

    NavigationBar(containerColor = Color(0xFF6200EE)) {
        val currentRoute = currentRoute(navController)

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0xFFBB86FC),
                    indicatorColor = Color(0xFF3700B3)
                )
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

data class BottomNavItem(val label: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<Pair<String, Double>>>,
    partners: List<Pair<String, Double>> // Передаем партнёров
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController, balance) }
        composable("balance") { BalanceScreen(navController, balance, transactions) }
        composable("transactions") { TransactionsScreen(navController, transactions) }
        composable("settings") { SettingsScreen(navController) }
        composable("transfer") { TransferScreen(navController, balance, transactions, partners) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, balance: MutableState<Double>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Карточка с балансом
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF6A5ACD), Color(0xFF483D8B))
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Ваш баланс",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "${balance.value} ₽",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Кнопки с иконками
        ButtonWithIcon(
            navController = navController,
            route = "transfer",
            text = "Сделать перевод",
            icon = Icons.Default.Send
        )

        ButtonWithIcon(
            navController = navController,
            route = "transactions",
            text = "Посмотреть транзакции",
            icon = Icons.Default.List
        )
    }
}

@Composable
fun ButtonWithIcon(
    navController: NavHostController,
    route: String,
    text: String,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate(route) }
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF6200EE), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun BalanceScreen(
    navController: NavHostController,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<Pair<String, Double>>> // Используем новый тип
) {
    val cashbackController = CashbackController()
    val cashback = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ваш баланс: ${balance.value} ₽", fontSize = 20.sp, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для расчета кэшбэка по всем транзакциям
        Button(onClick = {
            // Расчет кэшбэка по всем транзакциям
            val totalCashback = calculateTotalCashback(transactions.value)
            cashback.value = "Общий кэшбэк: ${-totalCashback} ₽"
        }) {
            Text(text = "Рассчитать общий кэшбэк")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cashback.value.isNotEmpty()) {
            Text(text = cashback.value, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад")
        }
    }
}

fun calculateTotalCashback(transactions: List<Pair<String, Double>>): Double {
    // Подсчитываем общий кэшбэк по всем транзакциям
    return transactions.sumOf {
        it.second // Суммируем процент кэшбэка по всем транзакциям
    }
}


@Composable
fun TransactionsScreen(navController: NavHostController, transactions: MutableState<MutableList<Pair<String, Double>>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Список транзакций", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        // Отображение списка транзакций
        transactions.value.forEach { transaction ->
            Text(
                text = "Расход: ${transaction.second} ₽, Партнёр: ${transaction.first}", // Показываем сумму расхода с минусом и название партнера
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Назад"
        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }
    }
}




@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Настройки",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        ButtonWithIcon(navController, route = "notifications", text = "Уведомления", icon = Icons.Default.Notifications)
        ButtonWithIcon(navController, route = "language", text = "Язык", icon = Icons.Default.Language)
    }
}

@Composable
fun TransferScreen(
    navController: NavHostController,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<Pair<String, Double>>>, // Список для хранения названия партнера и суммы расхода
    partners: List<Pair<String, Double>> // Список партнёров с их процентами
) {
    val transferAmount = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val partnerName = remember { mutableStateOf("") } // Для ввода имени партнера
    val selectedPartnerPercentage = remember { mutableStateOf(1.0) } // По умолчанию 1%
    val errorMessage = remember { mutableStateOf("") }

    // Функция для поиска партнера по имени
    fun getPartnerPercentage(partnerName: String): Double {
        val partner = partners.find { it.first.equals(partnerName, ignoreCase = true) }
        return partner?.second ?: 1.0 // Если партнёр найден, возвращаем его процент, если нет - 1%
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Перевод средств", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода суммы
        OutlinedTextField(
            value = transferAmount.value,
            onValueChange = { transferAmount.value = it },
            label = { Text("Сумма перевода") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода телефона
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Номер телефона") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле для ввода имени партнёра
        OutlinedTextField(
            value = partnerName.value,
            onValueChange = {
                partnerName.value = it
                selectedPartnerPercentage.value = getPartnerPercentage(it) // Обновляем процент при изменении имени
            },
            label = { Text("Партнёр") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Отправить перевод"
        Button(onClick = {
            val amount = transferAmount.value.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                errorMessage.value = "Введите корректную сумму"
                return@Button
            }

            if (balance.value >= amount) {
                balance.value -= amount
                val cashback = (amount * selectedPartnerPercentage.value) / 100
                transactions.value.add(
                    Pair(
                        partnerName.value, // Сохраняем название партнера
                        -amount // Вычитаем с баланса
                    )
                )

                errorMessage.value = "Перевод успешен! Текущий баланс: ${balance.value} ₽. Кэшбэк: $cashback ₽"
            } else {
                errorMessage.value = "Недостаточно средств для перевода"
            }
        }) {
            Text("Отправить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Сообщение об ошибке или успехе
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                fontSize = 16.sp,
                color = if (errorMessage.value.contains("успешен")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Назад"
        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }
    }
}

