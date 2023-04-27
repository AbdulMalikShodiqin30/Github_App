package com.example.mainactivity.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mainactivity.data.local.entity.EntityUser

@Dao
interface DaoUser {
    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getUsers(): LiveData<List<EntityUser>>

    @Query("SELECT * FROM users where isFavorite = 1")
    fun getFavoriteUsers(): LiveData<List<EntityUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(user: EntityUser)

    @Update
    fun updateUser(user: EntityUser)

    @Query("DELETE FROM users WHERE isFavorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username AND isFavorite = 1)")
    fun isUserFavorite(username: String): Boolean

    @Query("SELECT * FROM users WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<EntityUser>

}