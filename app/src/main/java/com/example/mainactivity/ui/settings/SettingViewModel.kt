package com.example.mainactivity.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val preferences: PreferencesSetting) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSettings().asLiveData()
    }

    fun saveThemeSettings(isDarkMode: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSettings(isDarkMode)
        }
    }
}