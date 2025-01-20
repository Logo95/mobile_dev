package com.example.bankapp.model

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object CashbackModel {
    private val client = OkHttpClient()

    fun fetchCashback(monthType: String, callback: (String) -> Unit) {
        val url = "http://10.0.2.2:5000/cashback?month=$monthType"
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (responseData != null) {
                    // Парсим JSON-ответ
                    val json = JSONObject(responseData)
                    val cashback = json.optDouble("cashback", 0.0) // Извлекаем значение "cashback"
                    callback(cashback.toString())
                } else {
                    callback("Ошибка: пустой ответ от сервера")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback("Ошибка соединения")
            }
        }.start()
    }
}
