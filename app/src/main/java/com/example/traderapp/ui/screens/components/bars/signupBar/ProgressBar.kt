package com.example.traderapp.ui.screens.components.bars.signupBar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ProgressBar(steps: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        steps.forEachIndexed { index, step ->
            StepItem(step = step, stepNumber = index + 1, isLast = index == steps.lastIndex)
        }
    }
}