package com.kotlin.twstock.data

import com.kotlin.twstock.data.api.RetrofitInstance
import com.kotlin.twstock.data.api.StockDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class StockRepository {
    private val api = RetrofitInstance.api

    suspend fun getStocks(): List<Stock> = coroutineScope {
        try {
            // Initiate all requests in parallel
            val stockDayDeferred = async { api.getStockDayAll() }
            val bwibbuDeferred = async { api.getBwibbuAll() }
            val avgDeferred = async { api.getStockDayAvgAll() }
            
            val validDtos = stockDayDeferred.await()
            val bwibbuList = bwibbuDeferred.await()
            val avgList = avgDeferred.await()

            // Map auxiliary data for faster lookup
            val bwibbuMap = bwibbuList.associateBy { it.code }
            val avgMap = avgList.associateBy { it.code }

            validDtos.map { dto ->
                val bwibbu = bwibbuMap[dto.code]
                val avg = avgMap[dto.code]
                
                Stock(
                    id = dto.code,
                    name = dto.name,
                    openPrice = dto.openingPrice,
                    closePrice = dto.closingPrice,
                    highPrice = dto.highestPrice,
                    lowPrice = dto.lowestPrice,
                    spread = dto.change?.takeIf { it.isNotEmpty() } ?: "-",
                    monthlyAvg = avg?.monthlyAveragePrice ?: "-",
                    txCount = dto.transaction,
                    txShares = dto.tradeVolume,
                    txAmount = dto.tradeValue,
                    peRatio = bwibbu?.peRatio ?: "無資料",
                    dividendYield = bwibbu?.dividendYield ?: "無資料",
                    pbRatio = bwibbu?.pbRatio ?: "無資料"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
