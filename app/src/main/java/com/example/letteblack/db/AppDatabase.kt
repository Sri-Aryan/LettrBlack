package com.example.letteblack.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [GroupMemberEntity::class, NoteEntity::class, GroupEntity::class, TaskEntity::class, UserEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun noteDao(): NoteDao
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
}

val Migration = object : Migration(3,4){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user_info ADD COLUMN avatarUri TEXT")
    }
}