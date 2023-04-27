package com.example.mainactivity.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mainactivity.R
import com.example.mainactivity.data.Result
import com.example.mainactivity.data.local.entity.EntityUser
import com.example.mainactivity.databinding.DetailActivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private var _binding: DetailActivityBinding? = null
    private val binding get() = _binding

    private var isFavorite: Boolean = false

    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val intent = intent
        val name = intent.getStringExtra(EXTRA_USERNAME).toString()

        val factory: ViewFactoryModel = ViewFactoryModel.getInstance(this@DetailActivity)
        val detailViewModel: ViewDetailModel by viewModels {
            factory
        }

        showSectionsPager()

        detailViewModel.getDetailUser(name).observe(this@DetailActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> binding?.progressBarr?.visibility =
                        View.VISIBLE
                    is Result.Success -> {
                        binding?.progressBarr?.visibility = View.GONE
                        val userData = result.data
                        userData.let { user ->
                            binding?.apply {
                                binding?.ivAvatarUser?.let {
                                    Glide.with(this@DetailActivity)
                                        .load(user.avatarUrl)
                                        .into(it)
                                }
                                tvUsername.text = user.login
                                tvName.text = user.name
                                binding?.following?.text ?:
                                    String.format(getString(R.string.follower), user.followers)
                               binding?.follower?.text ?:
                                    String.format(getString(R.string.following), user.following)
                            }
                        }
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

        supportActionBar?.apply {
            title = "Github Detail User"
            elevation = 0f
        }

        detailViewModel.getFavoriteUserByUsername(name).observe(this) { result ->
            detailViewModel.insertUser(name)
            if (result != null) {
                isFavorite = result.isFavorite
                if (isFavorite) {
                    binding?.tbFavorite?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            binding?.tbFavorite?.context!!,
                            R.drawable.ic_favorit_red
                        )
                    )
                } else {
                    binding?.tbFavorite?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            binding?.tbFavorite?.context!!,
                            R.drawable.ic_favorit_gray
                        )
                    )
                }
            }
        }

        binding?.tbFavorite?.setOnClickListener {
            detailViewModel.getDetailUser(name).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> binding?.progressBarr?.visibility = View.VISIBLE
                        is Result.Success -> {
                            val user = EntityUser(
                                result.data.login,
                                result.data.avatarUrl,
                                isFavorite,
                            )
                            if (user.isFavorite) {
                                detailViewModel.deleteFavoriteUser(user)
                            } else {
                                detailViewModel.saveFavoriteUser(user)
                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan: " + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showSectionsPager() {
        val intent = intent
        val name = intent.getStringExtra(EXTRA_USERNAME).toString()

        val sectionsPagerAdapter = AdapterPagerSection(this@DetailActivity, name)
        val viewPager: ViewPager2? = binding?.viewPager
        viewPager?.adapter = sectionsPagerAdapter
        val tabs: TabLayout? = binding?.tab
        TabLayoutMediator(tabs!!, viewPager!!) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}