package com.example.traderapp.ui.screens.trade


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.theme.TraderAppTheme

@Composable
fun ExchangeSuccessScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                title = stringResource(id = R.string.exchange_completed),
//                showInfoButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.transaction_success),
                contentDescription = stringResource(R.string.exchange_completed),
                modifier = Modifier
                    .size(350.dp)
                    .padding(16.dp)
            )

            AppTitle(
                text = stringResource(R.string.exchange_success),
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.exchange_detail),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExchangeAmountBox(
                leftCurrency = "Bitcoin BTC",
                leftAmount = "0.040141",
                rightCurrency = "Ethereum BTC",
                rightAmount = "0.689612"
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
            )


//            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = stringResource(R.string.done),
                modifier = Modifier
                    .padding(vertical = 14.dp),
                onClick = { navController.navigate("home") },
                backgroundColor = Color(0xFF2ECC71),
                textColor = Color.White
            )
        }
    }
}

@Composable
fun ExchangeAmountBox(
    leftCurrency: String,
    leftAmount: String,
    rightCurrency: String,
    rightAmount: String
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = leftCurrency, style = MaterialTheme.typography.labelSmall)
                Text(text = leftAmount, style = MaterialTheme.typography.titleMedium)
            }

            Image(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "Arrow",
                modifier = Modifier.size(50.dp)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(text = rightCurrency, style = MaterialTheme.typography.labelSmall)
                Text(text = rightAmount, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeSuccessScreenPreview() {
    TraderAppTheme {
        val navController = rememberNavController()
        ExchangeSuccessScreen(navController = navController)
    }
}

