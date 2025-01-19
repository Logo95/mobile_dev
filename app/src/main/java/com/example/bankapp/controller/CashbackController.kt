package com.example.bankapp.controller

import com.example.bankapp.model.CashbackModel

class CashbackController {
    fun getCashback(monthType: String, onResult: (String) -> Unit) {
        CashbackModel.fetchCashback(monthType) { cashback ->
            onResult(cashback)
        }
    }
}
