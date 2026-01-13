package com.kotlin.twstock.data.api

import com.google.gson.annotations.SerializedName

data class StockDto(
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String,
    @SerializedName("OpeningPrice") val openingPrice: String,
    @SerializedName("ClosingPrice") val closingPrice: String,
    @SerializedName("HighestPrice") val highestPrice: String,
    @SerializedName("LowestPrice") val lowestPrice: String,
    @SerializedName("Change") val change: String?,
    @SerializedName("TradeVolume") val tradeVolume: String,
    @SerializedName("TradeValue") val tradeValue: String,
    @SerializedName("Transaction") val transaction: String,
)

data class BwibbuDto(
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String,
    @SerializedName("PEratio") val peRatio: String, // 本益比
    @SerializedName("DividendYield") val dividendYield: String, // 殖利率
    @SerializedName("PBratio") val pbRatio: String // 股價淨值比
)

data class StockDayAvgDto(
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String,
    @SerializedName("MonthlyAveragePrice") val monthlyAveragePrice: String // 月平均價
)
