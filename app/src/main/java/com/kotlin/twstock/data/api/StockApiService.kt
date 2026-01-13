package com.kotlin.twstock.data.api

import retrofit2.http.GET

interface StockApiService {
    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): List<StockDto>

    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun getBwibbuAll(): List<BwibbuDto>

    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): List<StockDayAvgDto>
}
