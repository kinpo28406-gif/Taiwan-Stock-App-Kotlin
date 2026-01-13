package com.kotlin.twstock.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.twstock.data.Stock
import com.kotlin.twstock.data.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    private val repository = StockRepository()

    private var _allStocks = listOf<Stock>()

    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    val stocks: StateFlow<List<Stock>> = _stocks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        fetchStocks()
    }

    private fun fetchStocks() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getStocks()
            _allStocks = data.sortedByDescending { it.id }
            // Initial load
            currentPage = 0
            loadNextPage(reset = true)
            _isLoading.value = false
        }
    }
    
    fun loadNextPage(reset: Boolean = false) {
        if (reset) {
            _stocks.value = emptyList()
        }
        
        val start = currentPage * pageSize
        if (start >= _allStocks.size) return

        val end = minOf(start + pageSize, _allStocks.size)
        val newPage = _allStocks.subList(start, end)
        
        _stocks.value += newPage
        currentPage++
    }

    fun sortById(ascending: Boolean) {
        _allStocks = if (ascending) {
            _allStocks.sortedBy { it.id }
        } else {
            _allStocks.sortedByDescending { it.id }
        }
        // Reset pagination
        currentPage = 0
        loadNextPage(reset = true)
    }
}
