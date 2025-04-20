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
        val cryptoList by viewModel.cryptoList.collectAsState()
        val priceUpdates by viewModel.priceUpdates.collectAsState()
        var searchQuery by remember { mutableStateOf("") }

        var sortOrder by remember { mutableStateOf(SortOrder.DESCENDING) }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showFilterSheet by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        SetStatusBarColor()

        Scaffold(
            topBar = {
                AppTopBarHome(
                    navigationIconType = NavigationIconType.BACK,
                    rightIconType = RightIconType.UNION,
                    onBackClick = { navController.popBackStack() },
                    onRightClick = {
                        showFilterSheet = true
                        scope.launch { sheetState.show() }
                    },
                    title = "Market"
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
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
                    onSearchQueryChanged = { query -> searchQuery = query }
                )

                val filteredList = cryptoList
                    .filter { it.name.contains(searchQuery, ignoreCase = true) }
                    .let {
                        when (sortOrder) {
                            SortOrder.ASCENDING -> it.sortedBy { c -> c.priceUsd.toDoubleOrNull() ?: 0.0 }
                            SortOrder.DESCENDING -> it.sortedByDescending { c -> c.priceUsd.toDoubleOrNull() ?: 0.0 }
                        }
                    }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { MarketHeader() }

                    items(filteredList) { crypto ->
                        CryptoItem(
                            crypto = crypto,
                            currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDouble()
                        )
                    }
                }
            }

            if (showFilterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showFilterSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.outline
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


