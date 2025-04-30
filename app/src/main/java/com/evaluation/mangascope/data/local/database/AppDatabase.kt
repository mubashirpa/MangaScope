package com.evaluation.mangascope.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evaluation.mangascope.data.local.dao.UserDao
import com.evaluation.mangascope.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
