package com.example.traderapp.ui.screens.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.example.traderapp.R
import com.example.traderapp.ui.theme.LightOffline

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    isValid: Boolean = true,
    isPassword: Boolean = false,
    leadingIcon: (@Composable (Dp) -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    customTrailingIcon: Int = R.drawable.ic_check,
    iconSize: Dp = 34.dp
) {
    val isTouched = remember { mutableStateOf(false) }

    // VIS
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = {
            isTouched.value = true // while changing the text, flag isTouched turns true
            onValueChange(it)      // pass new value to onValueChange
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        isError = value.isNotEmpty() && !isValid,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = LightOffline,
        ),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            leadingIcon?.invoke(iconSize)
        },
        trailingIcon = {
            if (isTouched.value && !isValid) {

                Icon(
                    painter = painterResource(id = R.drawable.close_ring_light),
                    contentDescription = "invalid input",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(4.dp),
                    tint = Color.Unspecified
                )
            } else if (value.isNotEmpty() && isValid) {

                Icon(
                    painter = painterResource(id = customTrailingIcon),
                    contentDescription = "valid input",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(4.dp),
                    tint = Color.Unspecified
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

