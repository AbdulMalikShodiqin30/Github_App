package com.example.mainactivity.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.data.UserRepository
import com.example.mainactivity.di.Injection

class ViewModelFactoryFavorite private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFavorite::class.java)) {
            return ViewModelFavorite(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactoryFavorite? = null
        fun getInstance(context: Context): ViewModelFactoryFavorite =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactoryFavorite(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
