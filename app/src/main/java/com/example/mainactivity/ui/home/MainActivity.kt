package com.example.mainactivity.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainactivity.R
import com.example.mainactivity.data.Result
import com.example.mainactivity.data.remote.response.SearchItem
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.ui.detail.DetailActivity
import com.example.mainactivity.ui.favorite.ActivityFavorite
import com.example.mainactivity.ui.settings.PreferencesSetting
import com.example.mainactivity.ui.settings.SettingActivity
import com.example.mainactivity.ui.settings.SettingViewModel
import com.example.mainactivity.ui.settings.SettingViewModelFactory

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this@MainActivity)
        val homeViewModel: ViewMainModel by viewModels {
            factory
        }

        val pref = PreferencesSetting.getInstance(dataStore)
        val settingsViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        homeViewModel.getSearchedUser().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBarr?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBarr?.visibility = View.GONE
                        val userData = result.data
                        setUserData(userData)
                    }
                    is Result.Error -> {
                        binding?.progressBarr?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        val layoutMan = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutMan.orientation)
        binding?.rvHome?.apply {
            layoutManager = layoutMan
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this@MainActivity)
        val viewMainModel: ViewMainModel by viewModels {
            factory
        }

        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val favoriteMenu = menu.findItem(R.id.option_favorite)
        val settingsMenu = menu.findItem(R.id.option_setting)

        searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewMainModel.getSearchedUser(query.toString())
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        favoriteMenu.setOnMenuItemClickListener(this)
        settingsMenu.setOnMenuItemClickListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setUserData(searchItem: List<SearchItem>) {
        val userAdapter = AdapterUser(searchItem)
        binding?.rvHome?.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : AdapterUser.OnItemClickCallback {
            override fun onItemClicked(data: SearchItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.login)
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_favorite -> {
                Log.d("TESS", "Fav")
                val favoriteIntent = Intent(this@MainActivity, ActivityFavorite::class.java)
                startActivity(favoriteIntent)
            }
            R.id.option_setting -> {
                Log.d("TESS", "Settings")
                val settingsIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(settingsIntent)
            }
        }

        return true
    }
}