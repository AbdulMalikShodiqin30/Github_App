package com.example.mainactivity.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.databinding.SettingActivityBinding

class SettingActivity : AppCompatActivity() {
    private var _binding: SettingActivityBinding? = null
    private val binding get() = _binding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SettingActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = EXTRA_TITLE

        val pref = PreferencesSetting.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchTheme?.isChecked = false
            }
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSettings(isChecked)
        }
    }

    companion object {
        private const val EXTRA_TITLE = "Theme Settings"
    }
}