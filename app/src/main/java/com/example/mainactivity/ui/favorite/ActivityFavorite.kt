package com.example.mainactivity.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainactivity.data.local.entity.EntityUser
import com.example.mainactivity.databinding.FavoriteActivityBinding
import com.example.mainactivity.ui.detail.DetailActivity

class ActivityFavorite : AppCompatActivity() {
    private var _binding: FavoriteActivityBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FavoriteActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: ViewModelFactoryFavorite = ViewModelFactoryFavorite.getInstance(this)
        val favoriteViewModel: ViewModelFavorite by viewModels {
            factory
        }

        favoriteViewModel.getFavoriteUsers().observe(this) { result ->
            setUserData(result)
        }

        val layoutMan = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutMan.orientation)
        binding?.rvFavorite?.apply {
            layoutManager = layoutMan
            addItemDecoration(dividerItemDecoration)
        }

        supportActionBar?.title = "Favorite Users"
    }

    private fun setUserData(userList: List<EntityUser>) {
        val favoriteAdapter = AdapterFavorite(userList)
        binding?.rvFavorite?.adapter = favoriteAdapter
        favoriteAdapter.setOnItemClickCallback(object : AdapterFavorite.OnItemClickCallback {
            override fun onItemClicked(data: EntityUser) {
                val intent = Intent(this@ActivityFavorite, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.username)
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
}