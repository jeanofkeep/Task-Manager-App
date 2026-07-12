package com.example.test_app.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_app.ui.theme.AppTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.content.Context

private val Context.dataStore by preferencesDataStore(name = "settings")
private val THEME_KEY = stringPreferencesKey("app_theme")

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    var currentTheme by mutableStateOf(AppTheme.SlateAmber)
        private set

    init {
        viewModelScope.launch {
            context.dataStore.data.map { prefs ->
                prefs[THEME_KEY] ?: AppTheme.SlateAmber.name
            }.collect { themeName ->
                currentTheme = try {
                    AppTheme.valueOf(themeName)
                } catch (e: Exception) {
                    AppTheme.SlateAmber
                }
            }
        }
    }

    fun onThemeChange(newTheme: AppTheme) {
        currentTheme = newTheme
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[THEME_KEY] = newTheme.name
            }
        }
    }
}