package com.example.bankapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankapp.controller.CashbackController

@Composable
fun MainScreen() {
    val cashbackController = CashbackController()
    val balance = remember { mutableStateOf("10000 ₽") }
    val cashback = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваш баланс: ${balance.value}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 18.sp
            )

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
                Text(
                    text = "Кешбэк: ${cashback.value} ₽",
                    modifier = Modifier
                        .fillMaxWidth() // Растягиваем по ширине
                        .padding(16.dp),
                    fontSize = 18.sp, // Размер текста
                    maxLines = 1, // Ограничиваем одной строкой
                    overflow = TextOverflow.Clip // Предотвращаем перенос
                )
            }
        }
    }
}
