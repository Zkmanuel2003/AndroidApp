package com.example.testapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Auftrag::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun auftragDao(): AuftragDao
}