package com.example.mainactivity.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.mainactivity.data.local.entity.EntityUser
import com.example.mainactivity.data.local.room.DaoUser
import com.example.mainactivity.data.remote.response.ResponseDetailUser
import com.example.mainactivity.data.remote.response.ResponseFollow
import com.example.mainactivity.data.remote.response.ResponseSearch
import com.example.mainactivity.data.remote.response.SearchItem
import com.example.mainactivity.data.remote.retrofit.ServiceApi
import com.example.mainactivity.utils.ExecutorsApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ServiceApi,
    private val userDao: DaoUser,
    private val appExecutors: ExecutorsApp,
) {
    private val searchResult = MutableLiveData<Result<List<SearchItem>>>()
    private val followingResult = MutableLiveData<Result<List<ResponseFollow>>>()
    private val followerResult = MutableLiveData<Result<List<ResponseFollow>>>()
    private val detailUserResult = MutableLiveData<Result<ResponseDetailUser>>()
    private val userResult = MediatorLiveData<Result<EntityUser>>()

    fun getFollower(username: String): LiveData<Result<List<ResponseFollow>>> {
        followerResult.value = Result.Loading
        val client = apiService.getFollowers(username)
        client.enqueue(object : Callback<List<ResponseFollow>> {
            override fun onResponse(
                call: Call<List<ResponseFollow>>,
                response: Response<List<ResponseFollow>>
            ) {
                if (response.isSuccessful) {
                    followerResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ResponseFollow>>, t: Throwable) {
                followerResult.value = Result.Error(t.message.toString())
            }

        })

        return followerResult
    }

    fun getDetailUser(username: String): LiveData<Result<ResponseDetailUser>> {
        detailUserResult.value = Result.Loading
        val client = apiService.getUserByName(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                if (response.isSuccessful) {
                    detailUserResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                detailUserResult.value = Result.Error(t.message.toString())
            }
        })

        return detailUserResult
    }

    fun getFollowing(username: String): LiveData<Result<List<ResponseFollow>>> {
        followingResult.value = Result.Loading
        val client = apiService.getFollowing(username)
        client.enqueue(object : Callback<List<ResponseFollow>> {
            override fun onResponse(
                call: Call<List<ResponseFollow>>,
                response: Response<List<ResponseFollow>>
            ) {
                if (response.isSuccessful) {
                    followingResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ResponseFollow>>, t: Throwable) {
                followingResult.value = Result.Error(t.message.toString())
            }

        })

        return followingResult
    }

    fun getSearchedUser(username: String): LiveData<Result<List<SearchItem>>> {
        searchResult.value = Result.Loading
        val client = apiService.getUser(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                if (response.isSuccessful) {
                    appExecutors.mainThread.execute {
                        searchResult.value = Result.Success(response.body()!!.items)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                searchResult.value = Result.Error(t.message.toString())
            }

        })
        return searchResult
    }

    fun insertUser(username: String): LiveData<Result<EntityUser>> {
        val client = apiService.getUserByName(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    appExecutors.diskIO.execute {
                        val isFavorite = userDao.isUserFavorite(userResponse!!.login)
                        val user = EntityUser(
                            userResponse.login,
                            userResponse.avatarUrl,
                            isFavorite,
                        )
                        userDao.deleteAll()
                        userDao.insertUsers(user)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                userResult.value = Result.Error(t.message.toString())
            }
        })
        return userResult
    }

    fun getFavoriteUserByUsername(username: String): LiveData<EntityUser> {
        return userDao.getFavoriteUserByUsername(username)
    }

    fun getFavoriteUsers(): LiveData<List<EntityUser>> {
        return userDao.getFavoriteUsers()
    }

    fun setFavoriteUser(user: EntityUser, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorite = favoriteState
            userDao.updateUser(user)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ServiceApi,
            dao: DaoUser,
            appExecutors: ExecutorsApp
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, dao, appExecutors)
            }.also { instance = it }
    }
}