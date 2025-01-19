package com.example.bankapp.model

import okhttp3.OkHttpClient
import okhttp3.Request

object CashbackModel {
    private val client = OkHttpClient()

    fun fetchCashback(monthType: String, callback: (String) -> Unit) {
        val url = "http://10.0.2.2:5000/cashback?month=$monthType"
        //println("Запрос отправляется на: $url") // Лог для отладки

        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                //println("Ответ сервера: ${response.code}") // Лог статуса ответа
                val responseData = response.body?.string() ?: "Ошибка"
                callback(responseData)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("Ошибка соединения: ${e.message}")
            }
        }.start()
    }

}
