package com.example.mainactivity.di

import android.content.Context
import com.example.mainactivity.data.UserRepository
import com.example.mainactivity.data.local.room.DatabaseUsers
import com.example.mainactivity.data.remote.retrofit.ConfigApi
import com.example.mainactivity.utils.ExecutorsApp

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ConfigApi.getApiService()
        val database = DatabaseUsers.getInstance(context)
        val dao = database.DaoUser()
        val appExecutors = ExecutorsApp()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}