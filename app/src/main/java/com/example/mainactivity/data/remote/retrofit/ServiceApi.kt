package com.example.mainactivity.data.remote.retrofit

import com.example.mainactivity.data.remote.response.ResponseDetailUser
import com.example.mainactivity.data.remote.response.ResponseFollow
import com.example.mainactivity.data.remote.response.ResponseSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {
    @GET("/search/users")
    fun getUser(
        @Query("q") query: String
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun getUserByName(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseFollow>>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ResponseFollow>>
}