package com.example.traderapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.traderapp.ui.theme.AppTheme

@Composable
fun ThemeChooserDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = androidx.compose.ui.Modifier.padding(24.dp)) {
                Text(
                    text = "Select App Theme",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

                AppTheme.values().forEach { theme ->
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedTheme == theme,
                            onClick = {
                                selectedTheme = theme
                                onThemeSelected(theme)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                        Text(text = theme.name)
                    }
                }
            }
        }
    }
}
