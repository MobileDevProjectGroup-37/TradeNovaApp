@Composable
fun PortfolioItem(
    crypto: String,
    currentPrice: String,
    amount: String? = null,
    usdValue: String? = null,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    showHint: Boolean = false,
    hintText: String = "",
    changePercent: Double = 0.0,
    compact: Boolean = false // 👈 ключевая добавка
) {
    if (compact) {
        // 🔹 КОМПАКТНЫЙ ВАРИАНТ (для PortfolioSection)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { onClick() },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "$crypto logo",
                        tint = Color(0xFFF7931A),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = crypto,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$$currentPrice",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (changePercent >= 0) "+${String.format("%.2f", changePercent)}%" else "${String.format("%.2f", changePercent)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (changePercent >= 0) Color(0xFF5DDC71) else Color.Red
                    )
                }
            }
        }

    } else {
        // 🔸 ПОДРОБНЫЙ ВАРИАНТ (для BuyTab)
        val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 6.dp)
                    .background(
                        Color(0xFFD9D9D9).copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium
                    )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                    .border(1.5.dp, borderColor, MaterialTheme.shapes.medium),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = crypto, fontWeight = FontWeight.Bold)

                    amount?.let {
                        Text("Owned: $it")
                    }

                    usdValue?.let {
                        Text("Total value: \$$it")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1 $crypto ≈ \$$currentPrice",
                            color = MaterialTheme.colorScheme.secondary
                        )

                        if (showHint) {
                            Text(
                                text = if (selected) "✓ Selected" else hintText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
