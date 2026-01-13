package com.kotlin.twstock.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.twstock.data.Stock
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlin.twstock.ui.viewmodel.StockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(viewModel: StockViewModel = viewModel()) {
    val stocks by viewModel.stocks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    StockScreenContent(
        stocks = stocks,
        isLoading = isLoading,
        searchQuery = searchQuery,
        isDarkMode = isDarkMode,
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onToggleDarkMode = { viewModel.toggleDarkMode() },
        onSort = { ascending -> viewModel.sortById(ascending) },
        onLoadMore = { viewModel.loadNextPage() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreenContent(
    stocks: List<Stock>,
    isLoading: Boolean,
    searchQuery: String,
    isDarkMode: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onToggleDarkMode: () -> Unit,
    onSort: (Boolean) -> Unit,
    onLoadMore: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedStock by remember { mutableStateOf<Stock?>(null) }
    
    // Define colors based on isDarkMode
    val containerColor = if (isDarkMode) Color(0xFF121212) else Color.White
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFFCF5FC)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isDarkMode) Color.LightGray else Color.Gray

    Scaffold(
        topBar = {
            Surface(
                color = containerColor,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // Move down (top padding increased), reduce bottom space (bottom padding reduced)
                        .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dark Mode Toggle
                    IconButton(onClick = onToggleDarkMode) {
                        // Switch between Brightness7 (Sun/Day) and Brightness4 (Moon/Night)
                        // If currently Dark, show Sun to switch to Light? Or show Moon to indicate state?
                        // Typically: Show the icon of the mode you will SWITCH TO (i.e., opposite).
                        // If isDarkMode is TRUE, show SUN (to switch to light).
                        // If isDarkMode is FALSE, show MOON (to switch to dark).
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                            contentDescription = "Toggle Dark Mode",
                            tint = textColor
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("搜尋股票代碼或名稱", color = subTextColor) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            cursorColor = textColor,
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = subTextColor
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Menu Button (Existing)
                    IconButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = textColor
                        )
                    }
                }
            }
        },
        containerColor = containerColor
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = textColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(stocks) { stock ->
                    StockCard(
                        stock = stock, 
                        isDarkMode = isDarkMode,
                        cardColor = cardColor,
                        textColor = textColor,
                        subTextColor = subTextColor,
                        onClick = { selectedStock = stock }
                    )
                }
                item {
                    LaunchedEffect(true) {
                        onLoadMore()
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF8F0FA)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 8.dp)
                ) {
                    Text(
                        text = "依股票代號降序",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                onSort(false)
                                showBottomSheet = false 
                            }
                            .padding(16.dp),
                        fontSize = 18.sp,
                        color = textColor
                    )
                    Text(
                        text = "依股票代號升序",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                onSort(true)
                                showBottomSheet = false 
                            }
                            .padding(16.dp),
                        fontSize = 18.sp,
                        color = textColor
                    )
                }
            }
        }
        
        selectedStock?.let { stock ->
            AlertDialog(
                onDismissRequest = { selectedStock = null },
                containerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color.White,
                titleContentColor = textColor,
                textContentColor = textColor,
                title = { Text(text = "${stock.name} (${stock.id})") },
                text = {
                   Column {
                        Text(text = "月平均價: ${stock.monthlyAvg}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "本益比: ${stock.peRatio}")

                        // 判斷殖利率是否有資料
                        val yieldText = if (stock.dividendYield.isNullOrBlank() || stock.dividendYield == "無資料") {
                            "無資料"
                        } else {
                            "${stock.dividendYield}%"
                        }
                        Text(text = "殖利率: $yieldText")

                        Text(text = "股價淨值比: ${stock.pbRatio}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedStock = null }) {
                        Text("關閉", color = textColor)
                    }
                }
            )
        }
    }
}

@Composable
fun StockCard(
    stock: Stock, 
    isDarkMode: Boolean = false,
    cardColor: Color = Color(0xFFFCF5FC),
    textColor: Color = Color.DarkGray,
    subTextColor: Color = Color.Gray,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor), 
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID and Name
            Text(
                text = stock.id,
                fontSize = 12.sp,
                color = subTextColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stock.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Grid Stats
            Row(modifier = Modifier.fillMaxWidth()) {
                // Column 1
                Column(modifier = Modifier.weight(1f)) {
                    StatRow("開盤價", stock.openPrice, textColor, subTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("最高價", stock.highPrice, textColor, subTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("漲跌價差", stock.spread, getSpreadColor(stock.spread), subTextColor)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Column 2
                Column(modifier = Modifier.weight(1f)) {
                    StatRow("收盤價", stock.closePrice, getPriceCompareColor(stock.closePrice, stock.monthlyAvg), subTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("最低價", stock.lowPrice, textColor, subTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("月平均價", stock.monthlyAvg, textColor, subTextColor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Bottom Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomStatItem("成交筆數", "${stock.txCount} 筆", textColor, subTextColor)
                BottomStatItem("成交量", formatShares(stock.txShares), textColor, subTextColor)
                BottomStatItem("成交額", formatAmount(stock.txAmount), textColor, subTextColor)
            }
        }
    }
}

@Composable
fun StatRow(
    label: String, 
    value: String, 
    valueColor: Color, 
    labelColor: Color = Color.Gray
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            color = labelColor
        )
        // Value
        Text(
            text = "($value)", 
            fontSize = 16.sp,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BottomStatItem(
    label: String, 
    value: String,
    valueColor: Color = Color.Black,
    labelColor: Color = Color.Gray
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = valueColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    val mockStocks = listOf(
        Stock(
            id = "2330",
            name = "台積電",
            openPrice = "580.0",
            closePrice = "585.0",
            highPrice = "588.0",
            lowPrice = "578.0",
            spread = "+5.0",
            monthlyAvg = "570.0",
            txCount = "10,234",
            txShares = "23,456,789",
            txAmount = "13,605,937,620",
            peRatio = "20.5",
            dividendYield = "2.5",
            pbRatio = "3.2"
        ),
        Stock(
            id = "0050",
            name = "元大台灣50",
            openPrice = "128.5",
            closePrice = "129.0",
            highPrice = "129.2",
            lowPrice = "128.0",
            spread = "+0.5",
            monthlyAvg = "127.5",
            txCount = "5,432",
            txShares = "11,223,344",
            txAmount = "1,447,811,376",
            peRatio = "-",
            dividendYield = "3.1",
            pbRatio = "-"
        )
    )
    StockScreenContent(
        stocks = mockStocks,
        isLoading = false,
        searchQuery = "",
        isDarkMode = false,
        onSearchQueryChange = {},
        onToggleDarkMode = {},
        onSort = {},
        onLoadMore = {}
    )
}

fun parseDouble(value: String): Double? {
    return try {
        value.replace(",", "").replace("+", "").toDouble()
    } catch (e: NumberFormatException) {
        null
    }
}

fun getSpreadColor(spread: String): Color {
    val value = parseDouble(spread) ?: return Color.Black
    return when {
        value > 0 -> Color.Red
        value < 0 -> Color(0xFF00C853)
        else -> Color.Black
    }
}

fun getPriceCompareColor(price: String, compareTo: String): Color {
    val p = parseDouble(price) ?: return Color.Black
    val c = parseDouble(compareTo) ?: return Color.Black
    
    return when {
        p > c -> Color.Red
        p < c -> Color(0xFF00C853)
        else -> Color.Black
    }
}

fun formatShares(value: String): String {
    val num = parseDouble(value) ?: return value
    val lots = num / 1000
    // Format with commas, integer
    return java.text.NumberFormat.getIntegerInstance().format(lots.toInt()) + " 張"
}

fun formatAmount(value: String): String {
    val num = parseDouble(value) ?: return value
    return when {
         num >= 100_000_000 -> String.format("%.2f 億", num / 100_000_000)
         num >= 10_000 -> String.format("%.2f 萬", num / 10_000)
         else -> value
    }
}
