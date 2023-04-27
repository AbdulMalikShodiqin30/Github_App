package com.example.mainactivity.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.data.UserRepository
import com.example.mainactivity.di.Injection

class ViewFactoryModel(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewDetailModel::class.java)) {
            return ViewDetailModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ViewFollowModel::class.java)) {
            return ViewFollowModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewFactoryModel? = null
        fun getInstance(context: DetailActivity): ViewFactoryModel =
            instance ?: synchronized(this) {
                instance ?: ViewFactoryModel(Injection.provideRepository(context))
            }.also { instance = it }
    }
}