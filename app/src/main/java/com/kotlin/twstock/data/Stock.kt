package com.kotlin.twstock.data

data class Stock(
    val id: String,
    val name: String,
    val openPrice: String,
    val closePrice: String,
    val highPrice: String,
    val lowPrice: String,
    val spread: String,
    val monthlyAvg: String,
    val txCount: String,
    val txShares: String,
    val txAmount: String,
    val peRatio: String,       // 本益比
    val dividendYield: String, // 殖利率
    val pbRatio: String        // 股價淨值比
)
