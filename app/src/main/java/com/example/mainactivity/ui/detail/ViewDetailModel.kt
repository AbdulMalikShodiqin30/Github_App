package com.example.mainactivity.ui.detail

import androidx.lifecycle.ViewModel
import com.example.mainactivity.data.UserRepository
import com.example.mainactivity.data.local.entity.EntityUser

class ViewDetailModel(private val userRepository: UserRepository) : ViewModel() {
    fun getDetailUser(name: String) = userRepository.getDetailUser(name)

    fun insertUser(name: String) = userRepository.insertUser(name)

    fun getFavoriteUserByUsername(username: String) = userRepository.getFavoriteUserByUsername(username)

    fun saveFavoriteUser(user: EntityUser) {
        userRepository.setFavoriteUser(user, true)
    }

    fun deleteFavoriteUser(user: EntityUser) {
        userRepository.setFavoriteUser(user, false)
    }

}