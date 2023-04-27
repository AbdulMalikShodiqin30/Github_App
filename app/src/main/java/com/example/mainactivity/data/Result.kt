package com.example.mainactivity.data

import com.example.mainactivity.data.remote.response.ResponseFollow

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}