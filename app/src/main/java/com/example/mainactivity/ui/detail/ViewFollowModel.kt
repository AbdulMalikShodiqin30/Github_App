package com.example.mainactivity.ui.detail

import androidx.lifecycle.ViewModel
import com.example.mainactivity.data.UserRepository

class ViewFollowModel(private val userRepository: UserRepository) :
    ViewModel() {

        fun getFollowing(username: String) = userRepository.getFollowing(username)

        fun getFollowers(username: String) = userRepository.getFollower(username)
}