package com.example.mainactivity.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.data.UserRepository

class ViewModelFavorite(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUsers() = userRepository.getFavoriteUsers()
}
