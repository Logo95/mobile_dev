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
    val navController = rememberNavController()  // Убедись, что создается NavHostController

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Банковское приложение") }
            )
        }
    ) { innerPadding ->
        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,  // Используем NavHostController
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController) }
        composable("balance") { BalanceScreen(navController) }
        composable("transactions") { TransactionsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
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
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun BalanceScreen(navController: NavHostController) {
    val cashbackController = CashbackController()
    val balance = remember { mutableStateOf("10000 ₽") }
    val cashback = remember { mutableStateOf("") }

    // Убираем LaunchedEffect, чтобы кэшбэк не показывался автоматически

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ваш баланс: ${balance.value}", fontSize = 20.sp, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для кешбэка за текущий месяц
        Button(onClick = {
            cashbackController.getCashback("current") {
                cashback.value = it
            }
        }) {
            Text(text = "Кешбэк за текущий месяц")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка для кешбэка за прошлый месяц
        Button(onClick = {
            cashbackController.getCashback("previous") {
                cashback.value = it
            }
        }) {
            Text(text = "Кешбэк за прошлый месяц")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Показываем кэшбэк только после того, как он будет получен
        if (cashback.value.isNotEmpty()) {
            Text(text = "Кешбэк: ${cashback.value} ₽", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка для возврата на предыдущий экран
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад")
        }
    }
}

@Composable
fun TransactionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Список транзакций", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        // Здесь можно добавить динамические транзакции
        Text("Транзакция 1: -200 ₽", fontSize = 18.sp)
        Text("Транзакция 2: +1500 ₽", fontSize = 18.sp)
        Text("Транзакция 3: -500 ₽", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Назад")
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
