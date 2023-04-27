package com.example.mainactivity.ui.home

import androidx.lifecycle.ViewModel
import com.example.mainactivity.data.UserRepository


class ViewMainModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSearchedUser(username: String = "Abdul") = userRepository.getSearchedUser(username)
}
