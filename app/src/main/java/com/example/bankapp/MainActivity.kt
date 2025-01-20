package com.example.bankapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.bankapp.controller.CashbackController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState


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
    val transactions = remember { mutableStateOf(mutableListOf<String>()) } // Глобальный список транзакций

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Банковское приложение") }
            )
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            balance = balance,
            transactions = transactions // Передаём список транзакций
        )
    }
}



@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    balance: MutableState<Double>,
    transactions: MutableState<MutableList<String>> // Передаём список транзакций
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController) }
        composable("balance") { BalanceScreen(navController, balance) }
        composable("transactions") { TransactionsScreen(navController, transactions) } // Передаём транзакции
        composable("settings") { SettingsScreen(navController) }
        composable("transfer") { TransferScreen(navController, balance, transactions) } // Передаём транзакции
    }
}



@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Добро пожаловать в приложение", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        Button(onClick = { navController.navigate("balance") }) {
            Text("Посмотреть баланс и кешбэк")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("transactions") }) {
            Text("Посмотреть транзакции")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("settings") }) {
            Text("Настройки")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("transfer") }) { // Кнопка для перехода на экран перевода
            Text("Перевод")
        }
    }
}


@SuppressLint("RememberReturnType")
@Composable
fun BalanceScreen(navController: NavHostController, balance: MutableState<Double>) {
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

        Button(onClick = {
            cashbackController.getCashback("current") {
                cashback.value = it
            }
        }) {
            Text(text = "Кешбэк за текущий месяц")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            cashbackController.getCashback("previous") {
                cashback.value = it
            }
        }) {
            Text(text = "Кешбэк за прошлый месяц")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cashback.value.isNotEmpty()) {
            Text(text = "Кешбэк: ${cashback.value} ₽", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад")
        }
    }
}


@Composable
fun TransactionsScreen(navController: NavHostController, transactions: MutableState<MutableList<String>>) {
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
            Text(transaction, fontSize = 18.sp, modifier = Modifier.padding(vertical = 4.dp))
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Настройки", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        // Здесь могут быть параметры настроек
        Text("Язык", fontSize = 18.sp)
        Text("Уведомления", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад")
        }
    }
}

@Composable
fun TransferScreen(navController: NavHostController, balance: MutableState<Double>, transactions: MutableState<MutableList<String>>) {
    val transferAmount = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

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
                transactions.value.add("-${amount} ₽") // Добавляем запись о переводе
                errorMessage.value = "Перевод успешен! Текущий баланс: ${balance.value} ₽"
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


