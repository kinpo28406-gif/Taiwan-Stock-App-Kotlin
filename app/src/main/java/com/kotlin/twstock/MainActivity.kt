package com.kotlin.twstock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import com.kotlin.twstock.ui.theme.TwstockSimpleAppTheme
import com.kotlin.twstock.ui.screen.StockScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: com.kotlin.twstock.ui.viewmodel.StockViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            val isDarkMode = viewModel.isDarkMode.collectAsState().value
            
            TwstockSimpleAppTheme(darkTheme = isDarkMode) {
                // StockScreen handles its own Scaffold/Surface structure as needed, 
                // but usually we want a high level container.
                // StockScreen has a Scaffold, so we can just call it.
                StockScreen(viewModel = viewModel)
            }
        }
    }
}