package com.example.bankapp

import com.example.bankapp.controller.CashbackController
import com.example.bankapp.network.ExchangeRateApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.TextFieldDefaults

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
    val savingsBalance = remember { mutableStateOf(20000.0) } // Накопительный счёт
    val transactions = remember { mutableStateOf(mutableListOf<Pair<String, Double>>()) } // Глобальный список транзакций (название партнера + процент)
    val loanState = remember { mutableStateOf(LoanState(remainingLoanAmount = 7000.0, isPaidOff = false)) } // Глобальное состояние кредита

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
                title = { Text("Банковское приложение") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
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
            partners = partners,
            loanState = loanState, // Передаем состояние кредита
            savingsBalance = savingsBalance
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

data class LoanState(
    var remainingLoanAmount: Double,
    var isPaidOff: Boolean
)

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
    partners: List<Pair<String, Double>>, // Передаем партнёров
    loanState: MutableState<LoanState>,
    savingsBalance: MutableState<Double>
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController, balance) }
        composable("balance") { BalanceScreen(navController, balance, transactions, partners) }
        composable("transactions") { TransactionsScreen(navController, transactions) }
        composable("settings") { SettingsScreen(navController) }
        composable("transfer") { TransferScreen(navController, balance, transactions, partners) }
        composable("savings") { SavingsScreen(navController, balance, savingsBalance) }
        composable("loans") { LoanScreen(navController, balance, loanState) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, balance: MutableState<Double>) {
    val exchangeRates = remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    // Загрузка курсов валют
    LaunchedEffect(Unit) {
        try {
            val rates = ExchangeRateApi.getExchangeRates() // Вызов метода, а не объекта
            exchangeRates.value = rates
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Карточка с балансом
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${balance.value} ₽",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Блок для отображения баланса в других валютах
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "Баланс в других валютах",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            val usdRate = exchangeRates.value["USD"] ?: 1.0
            val eurRate = exchangeRates.value["EUR"] ?: 1.0

            // Доллары
            Text(
                text = "3000 USD (${String.format("%.2f", 3000 * usdRate)} ₽)",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            // Евро
            Text(
                text = "1500 EUR (${String.format("%.2f", 1500 * eurRate)} ₽)",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )
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

        // Новые кнопки
        ButtonWithIcon(
            navController = navController,
            route = "savings",
            text = "Накопительный счёт",
            icon = Icons.Default.AttachMoney
        )

        ButtonWithIcon(
            navController = navController,
            route = "loans",
            text = "Погашение кредитов",
            icon = Icons.Default.MoneyOff
        )
    }
}



@Composable
fun ButtonWithIcon(
    navController: NavHostController,
    route: String?,
    text: String,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3700B3))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    if (route != null) {
                        navController.navigate(route)
                    } else {
                        // Если route == null, не делаем ничего (кнопка-пустышка)
                    }
                }
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@SuppressLint("RememberReturnType")
@Composable
fun BalanceScreen(
    navController: NavHostController,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<Pair<String, Double>>>,
    partners: List<Pair<String, Double>> // Список партнеров
) {
    val cashback = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ваш баланс: ${balance.value} ₽",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF6200EE)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для расчета кэшбэка по всем транзакциям
        Button(
            onClick = {
                val totalCashback = calculateTotalCashback(transactions.value) { partnerName ->
                    partners.find { it.first == partnerName }?.second ?: 1.0
                }
                cashback.value = "Общий кэшбэк: $totalCashback ₽"
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Рассчитать общий кэшбэк", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cashback.value.isNotEmpty()) {
            Text(text = cashback.value, fontSize = 18.sp, color = Color(0xFF6200EE))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад", color = Color(0xFF6200EE))
        }
    }
}


fun calculateTotalCashback(transactions: List<Pair<String, Double>>, getPartnerPercentage: (String) -> Double): Double {
    return transactions.sumOf { transaction ->
        val cashbackPercentage: Double = getPartnerPercentage(transaction.first) // Получаем процент для партнера
        val amountSpent: Double = -transaction.second // Сумма расхода (отрицательная)
        (amountSpent * cashbackPercentage) / 100 // Рассчитываем кэшбэк
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
    val isNotificationsEnabled = remember { mutableStateOf(true) } // Состояние уведомлений

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

        // Кнопка переключения уведомлений
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3700B3))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        // Переключение состояния уведомлений
                        isNotificationsEnabled.value = !isNotificationsEnabled.value
                    }
            ) {
                Icon(
                    imageVector = if (isNotificationsEnabled.value) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                    contentDescription = "Уведомления",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (isNotificationsEnabled.value) "Уведомления включены" else "Уведомления отключены",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Кнопка для переключения языка
        ButtonWithIcon(navController, route = "language", text = "Язык", icon = Icons.Default.Language)
    }
}


@Composable
fun TransferScreen(
    navController: NavHostController,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<Pair<String, Double>>>,
    partners: List<Pair<String, Double>>
) {
    val transferAmount = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val partnerName = remember { mutableStateOf("") }
    val selectedPartnerPercentage = remember { mutableStateOf(1.0) }
    val errorMessage = remember { mutableStateOf("") }

    fun getPartnerPercentage(partnerName: String): Double {
        val partner = partners.find { it.first.equals(partnerName, ignoreCase = true) }
        return partner?.second ?: 1.0
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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода телефона
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Номер телефона") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле для ввода имени партнёра
        OutlinedTextField(
            value = partnerName.value,
            onValueChange = {
                partnerName.value = it
                selectedPartnerPercentage.value = getPartnerPercentage(it)
            },
            label = { Text("Партнёр") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                        partnerName.value,
                        -amount
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

@Composable
fun SavingsScreen(navController: NavHostController, balance: MutableState<Double>, savingsBalance: MutableState<Double>) {
    val withdrawalAmount = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Накопительный счёт",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        // Карточка с деталями накопительного счёта
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Текущая сумма на счёте",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.2f", savingsBalance.value)} ₽",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Процент доходности: 4.5% годовых",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ввода суммы снятия
        OutlinedTextField(
            value = withdrawalAmount.value,
            onValueChange = { withdrawalAmount.value = it },
            label = { Text("Введите сумму для снятия") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Снять"
        Button(
            onClick = {
                val amountToWithdraw = withdrawalAmount.value.toDoubleOrNull()
                if (amountToWithdraw == null || amountToWithdraw <= 0) {
                    errorMessage.value = "Введите корректную сумму"
                    return@Button
                }

                if (amountToWithdraw > savingsBalance.value) {
                    errorMessage.value = "Недостаточно средств на накопительном счёте"
                } else {
                    savingsBalance.value -= amountToWithdraw
                    balance.value += amountToWithdraw
                    errorMessage.value = "Снятие успешно! ${String.format("%.2f", amountToWithdraw)} ₽ добавлено на баланс"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Снять",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Сообщение об ошибке или успехе
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                fontSize = 16.sp,
                color = if (errorMessage.value.contains("успешно")) Color(0xFF4CAF50) else Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Кнопка "Назад"
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Назад",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun LoanScreen(navController: NavHostController, balance: MutableState<Double>, loanState: MutableState<LoanState>) {
    val repaymentAmount = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Погашение кредита",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        // Карточка с деталями кредита
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = if (loanState.value.isPaidOff) Color(0xFF4CAF50) else Color(0xFFB71C1C) // Зелёный, если кредит погашен
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (loanState.value.isPaidOff) "Кредит погашен!" else "Текущая сумма долга",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if (!loanState.value.isPaidOff) {
                    Text(
                        text = "${String.format("%.2f", loanState.value.remainingLoanAmount)} ₽",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Кредит: Личный займ (до 01.12.2025)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Поле для ввода суммы погашения
        OutlinedTextField(
            value = repaymentAmount.value,
            onValueChange = { repaymentAmount.value = it },
            label = { Text("Введите сумму для погашения") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedLabelColor = Color(0xFFB71C1C),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFFB71C1C),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Погасить кредит"
        Button(
            onClick = {
                val amountToPay = repaymentAmount.value.toDoubleOrNull()
                if (amountToPay == null || amountToPay <= 0) {
                    errorMessage.value = "Введите корректную сумму"
                    return@Button
                }

                when {
                    amountToPay > balance.value -> {
                        errorMessage.value = "Недостаточно средств для погашения"
                    }
                    amountToPay >= loanState.value.remainingLoanAmount -> {
                        balance.value -= loanState.value.remainingLoanAmount
                        loanState.value = loanState.value.copy(
                            remainingLoanAmount = 0.0,
                            isPaidOff = true
                        )
                        errorMessage.value = "Кредит полностью погашен!"
                    }
                    else -> {
                        loanState.value = loanState.value.copy(
                            remainingLoanAmount = loanState.value.remainingLoanAmount - amountToPay
                        )
                        balance.value -= amountToPay
                        errorMessage.value = "Платеж успешен! Осталось погасить: ${String.format("%.2f", loanState.value.remainingLoanAmount)} ₽"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Погасить кредит",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Сообщение об ошибке или успехе
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                fontSize = 16.sp,
                color = if (errorMessage.value.contains("успешен") || errorMessage.value.contains("погашен")) Color(0xFF4CAF50) else Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Кнопка "Назад"
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Назад",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
