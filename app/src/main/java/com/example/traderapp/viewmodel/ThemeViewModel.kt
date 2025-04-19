package com.example.traderapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.traderapp.ui.theme.AppTheme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {

    private val _theme = MutableStateFlow(AppTheme.SystemDefault)
    val theme: StateFlow<AppTheme> = _theme

    fun setTheme(newTheme: AppTheme) {
        _theme.value = newTheme
    }
}

