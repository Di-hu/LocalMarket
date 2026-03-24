package com.example.localmarket

data class OrderModel(
    val id: String,
    val items: String,
    val total: String,
    val address: String,
    var status: String,
    val time: Long
)