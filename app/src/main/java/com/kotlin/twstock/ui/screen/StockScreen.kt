package com.kotlin.twstock.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen() {
    val stocks = remember {
        listOf(
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
                txAmount = "13,605,937,620"
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
                txAmount = "1,447,811,376"
            ),
             Stock(
                id = "2603",
                name = "長榮",
                openPrice = "155.0",
                closePrice = "153.5",
                highPrice = "156.0",
                lowPrice = "153.0",
                spread = "-1.5",
                monthlyAvg = "150.0",
                txCount = "8,765",
                txShares = "15,678,901",
                txAmount = "2,406,711,303"
            )
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
               // Title or empty specific area? Image shows just a menu button top right.
               // It seems the white area extends.
               // We will put the menu button top right.
               IconButton(
                   onClick = { showBottomSheet = true },
                   modifier = Modifier
                       .align(Alignment.TopEnd)
                       .size(48.dp)
               ) {
                   Icon(
                       imageVector = Icons.Default.Menu,
                       contentDescription = "Menu",
                       tint = Color.Black
                   )
               }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(stocks) { stock ->
                StockCard(stock)
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFFF8F0FA) // Light purple background for sheet
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 8.dp)
                ) {
                     // The drag handle is default
                    Text(
                        text = "依股票代號降序",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBottomSheet = false }
                            .padding(16.dp),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "依股票代號升序",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBottomSheet = false }
                            .padding(16.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StockCard(stock: Stock) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF5FC)), // Very light pink/purple
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID and Name
            Text(
                text = stock.id,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stock.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Grid Stats
            Row(modifier = Modifier.fillMaxWidth()) {
                // Column 1
                Column(modifier = Modifier.weight(1f)) {
                    StatRow("開盤價", stock.openPrice, Color(0xFF00C853))
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("最高價", stock.highPrice, Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("漲跌價差", stock.spread, Color(0xFF00C853))
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Column 2
                Column(modifier = Modifier.weight(1f)) {
                    StatRow("收盤價", stock.closePrice, Color(0xFF00C853))
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("最低價", stock.lowPrice, Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow("月平均價", stock.monthlyAvg, Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Bottom Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomStatItem("成交筆數", "(${stock.txCount})")
                BottomStatItem("成交股數", "(${stock.txShares})")
                BottomStatItem("成交金額", "(${stock.txAmount})")
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        // Value with parentheses if needed, but per my previous plan, I was mimicking the style.
        // The image shows (Value) for the grid items too?
        // "開盤價 (開盤價)" - The layout shows standard Label + Value. 
        // Wait, looking at the image: "開盤價      (開盤價)"
        // It seems the value is enclosed in parentheses in the image.
        // I will add parentheses to the value display logic here to match the image precisely.
        Text(
            text = "($value)", 
            fontSize = 16.sp,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BottomStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    StockScreen()
}
