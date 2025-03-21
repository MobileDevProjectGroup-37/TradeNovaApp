package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.traderapp.R
import com.example.traderapp.ui.theme.LightOffline
import com.example.traderapp.ui.theme.LightUp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    isValid: Boolean = true,
    isPassword: Boolean = false,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    customTrailingIcon: Int = R.drawable.ic_check
) {

    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(label) },
        isError = value.isNotEmpty() && !isValid,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = LightOffline,
        ),
        shape = RoundedCornerShape(12.dp),

        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = label, tint = MaterialTheme.colorScheme.primary) }
        },
        trailingIcon = {
            if (value.isNotEmpty() && isValid) {
                Icon(
                    painter = painterResource(id = customTrailingIcon),
                    contentDescription = "valid input",
                    modifier = Modifier
                        .size(34.dp)
                        .padding(4.dp),
                    tint = LightUp
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,

    )
}
