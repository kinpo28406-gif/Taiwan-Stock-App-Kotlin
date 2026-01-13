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

    private var _allStocks = listOf<Stock>() // Only changes on fetch
    private var _filteredStocks = listOf<Stock>() // Changes on search or sort

    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    val stocks: StateFlow<List<Stock>> = _stocks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var isAscending = false // Track sort order

    init {
        fetchStocks()
    }

    private fun fetchStocks() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getStocks()
            // Initial load - default sort descending
            _allStocks = data
            _filteredStocks = data.sortedByDescending { it.id }
            
            currentPage = 0
            loadNextPage(reset = true)
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
    
    // Core filtering logic
    private fun applyFilters() {
        val query = _searchQuery.value
        val baseList = if (query.isBlank()) {
            _allStocks
        } else {
            _allStocks.filter { it.id.contains(query) || it.name.contains(query) }
        }

        _filteredStocks = if (isAscending) {
            baseList.sortedBy { it.id }
        } else {
            baseList.sortedByDescending { it.id }
        }

        // Reset pagination whenever filters change
        currentPage = 0
        loadNextPage(reset = true)
    }

    fun loadNextPage(reset: Boolean = false) {
        if (reset) {
            _stocks.value = emptyList()
        }
        
        val start = currentPage * pageSize
        if (start >= _filteredStocks.size) return

        val end = minOf(start + pageSize, _filteredStocks.size)
        val newPage = _filteredStocks.subList(start, end)
        
        _stocks.value += newPage
        currentPage++
    }

    fun sortById(ascending: Boolean) {
        isAscending = ascending
        applyFilters() // Re-apply sort on current filtered list
    }
}
