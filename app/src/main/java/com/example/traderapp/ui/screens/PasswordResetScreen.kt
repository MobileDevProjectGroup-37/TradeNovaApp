package com.example.traderapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traderapp.ui.screens.components.CustomTextField
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.CustomButton

@Composable
fun PasswordResetScreen(
    onGoogleClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    )


    {
        // Title
        Text(
            text = "Password reset",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,

        )



        // Instruction Text
        Text(
            text = "Please enter your registered email address to reset your password",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(top = 16.dp)
//                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))



        // Email Address Input
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email address",
            leadingIcon = Icons.Filled.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

        )
        Spacer(modifier = Modifier.height(140.dp))

        // Continue Button
        Button(
            onClick = { /* Handle continue action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Continue", color = Color.White)
        }

        Spacer(modifier = Modifier.height(30.dp))


        // Terms and Conditions Text
        Text(
            text = "By registering you accept our Terms & Conditions and Privacy Policy. Your data will be security encrypted with TLS",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = " or ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Social Media Icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )  {
            CustomButton(
                onClick = onGoogleClick,
                backgroundColor = Color.White,
                textColor = Color.Black,
                icon = painterResource(id = R.drawable.google_logo),
                fillImage = true,
                rounded = 8.dp,
                paddingNeeded = false,
                buttonWidth = 232.dp,
                buttonHeight = 48.dp
            )
            Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordResetScreen() {
    PasswordResetScreen()
}
