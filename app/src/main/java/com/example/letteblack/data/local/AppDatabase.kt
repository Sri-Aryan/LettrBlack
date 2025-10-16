package com.example.letteblack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.letteblack.data.local.dao.GroupDao
import com.example.letteblack.data.local.entities.GroupEntity
import com.example.letteblack.data.local.dao.GroupMemberDao
import com.example.letteblack.data.local.entities.GroupMemberEntity
import com.example.letteblack.data.local.dao.NoteDao
import com.example.letteblack.data.local.entities.NoteEntity
import com.example.letteblack.data.local.dao.TaskDao
import com.example.letteblack.data.local.entities.TaskEntity
import com.example.letteblack.data.local.dao.UserDao
import com.example.letteblack.data.local.entities.UserEntity


@Database(entities = [GroupMemberEntity::class, NoteEntity::class, GroupEntity::class, TaskEntity::class, UserEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun noteDao(): NoteDao
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
}