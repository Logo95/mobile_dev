package com.example.bankapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.example.bankapp.BuildConfig

object ExchangeRateApi {
    private const val BASE_URL = "https://api.fastforex.io/fetch-all"
    private val client = OkHttpClient()

    fun getExchangeRates(): Map<String, Double> {
        val request = Request.Builder()
            .url("$BASE_URL?api_key=${BuildConfig.EXCHANGE_RATE_API_KEY}") // Используем ваш API-ключ
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Ошибка сети: ${response.code}")

        val jsonString = response.body?.string()
        val jsonObject = JSONObject(jsonString ?: throw Exception("Пустой ответ от сервера"))
        val ratesObject = jsonObject.getJSONObject("results")

        // Преобразуем курсы валют в Map<String, Double>
        val rates = mutableMapOf<String, Double>()
        ratesObject.keys().forEach { currency ->
            rates[currency] = ratesObject.getDouble(currency)
        }
        return rates
    }
}
