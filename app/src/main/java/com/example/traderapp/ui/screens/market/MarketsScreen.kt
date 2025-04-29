package com.example.traderapp.ui.screens.market

import SetStatusBarColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.cards.CryptoItem
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.screens.components.bars.SearchBar
import com.example.traderapp.viewmodel.CryptoViewModel
import kotlinx.coroutines.launch

enum class SortOrder {
    ASCENDING,
    DESCENDING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(navController: NavController, viewModel: CryptoViewModel) {
    val cryptoList by viewModel.cryptoList.collectAsState() // Observe the static list of cryptos.
    val priceUpdates by viewModel.priceUpdates.collectAsState() // Observe the live price updates.
    var searchQuery by remember { mutableStateOf("") } // Local state for search input.

    var sortOrder by remember { mutableStateOf(SortOrder.DESCENDING) } // Local state for sorting order.

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // Bottom sheet state for filters.
    var showFilterSheet by remember { mutableStateOf(false) } // Control visibility of the filter sheet.
    val scope = rememberCoroutineScope() // Coroutine scope to launch bottom sheet animations.

    SetStatusBarColor() // Custom function to set the system status bar color.

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK, // Top bar back button.
                rightIconType = RightIconType.UNION, // Top bar filter button.
                onBackClick = { navController.popBackStack() }, // Navigate back when back button clicked.
                onRightClick = {
                    showFilterSheet = true // Show the bottom sheet for filters.
                    scope.launch { sheetState.show() }
                },
                title = "Market" // Title of the screen.
            )
        },
        bottomBar = {
            BottomNavigationBar(navController) // Display bottom navigation bar.
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = { query -> searchQuery = query } // Update search query state.
            )

            val filteredList = cryptoList
                .filter { it.name.contains(searchQuery, ignoreCase = true) } // Filter cryptos based on search query.
                .let {
                    when (sortOrder) {
                        SortOrder.ASCENDING -> it.sortedBy { c -> c.priceUsd.toDoubleOrNull() ?: 0.0 } // Sort ascending by price.
                        SortOrder.DESCENDING -> it.sortedByDescending { c -> c.priceUsd.toDoubleOrNull() ?: 0.0 } // Sort descending by price.
                    }
                }

            LazyColumn(modifier = Modifier.fillMaxSize()) { // Lazy loading list of cryptos.
                item { MarketHeader() } // Static header.

                items(filteredList) { crypto -> // Render each crypto item.
                    CryptoItem(
                        crypto = crypto,
                        currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDouble() // Show live price if available.
                    )
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false }, // Hide bottom sheet on dismiss.
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.outline // Set container color.
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FilterOption(
                        label = "Sort by Price ↑",
                        isSelected = sortOrder == SortOrder.ASCENDING
                    ) {
                        sortOrder = SortOrder.ASCENDING
                        scope.launch { sheetState.hide() }
                        showFilterSheet = false
                    }

                    FilterOption(
                        label = "Sort by Price ↓",
                        isSelected = sortOrder == SortOrder.DESCENDING
                    ) {
                        sortOrder = SortOrder.DESCENDING
                        scope.launch { sheetState.hide() }
                        showFilterSheet = false
                    }
                }
            }
        }
    }
}
