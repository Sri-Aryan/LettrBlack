package com.example.letteblack.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [GroupMemberEntity::class, NoteEntity::class, GroupEntity::class, TaskEntity::class, UserEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun noteDao(): NoteDao
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
}