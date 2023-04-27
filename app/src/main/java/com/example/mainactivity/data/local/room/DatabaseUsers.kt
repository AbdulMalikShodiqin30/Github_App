package com.example.mainactivity.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mainactivity.data.local.entity.EntityUser

@Database(entities = [EntityUser::class], version = 4, exportSchema = false)
abstract class DatabaseUsers : RoomDatabase() {
    abstract fun DaoUser(): DaoUser

    companion object {
        @Volatile
        private var INSTANCE: DatabaseUsers? = null
        fun getInstance(context: Context): DatabaseUsers =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseUsers::class.java,
                    "Users.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}