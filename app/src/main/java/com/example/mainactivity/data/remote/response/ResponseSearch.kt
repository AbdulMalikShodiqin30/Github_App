package com.example.mainactivity.data.remote.response

import com.google.gson.annotations.SerializedName

class ResponseSearch (

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<SearchItem>
)

data class SearchItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    )