package com.example.letteblack.core.di

import android.content.Context
import androidx.room.Room
import com.example.letteblack.data.local.AppDatabase
import com.example.letteblack.data.local.dao.GroupDao
import com.example.letteblack.data.local.dao.GroupMemberDao
import com.example.letteblack.data.local.dao.NoteDao
import com.example.letteblack.data.local.dao.TaskDao
import com.example.letteblack.data.local.dao.UserDao
import com.example.letteblack.domain.repository.GroupMemberRepository
import com.example.letteblack.data.repository.groupmembers.GroupMemberRepositoryImpl
import com.example.letteblack.domain.repository.GroupRepository
import com.example.letteblack.data.repository.groups.GroupRepositoryImpl
import com.example.letteblack.domain.repository.NoteRepository
import com.example.letteblack.data.repository.notes.NoteRepositoryImpl
import com.example.letteblack.domain.repository.TaskRepository
import com.example.letteblack.data.repository.tasks.TaskRepositoryImpl
import com.example.letteblack.domain.repository.UserRepository
import com.example.letteblack.data.repository.user.UserRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "study_app.db")
            .fallbackToDestructiveMigration()
            .build()
    @Provides
    fun provideGroupMemberDao(db: AppDatabase): GroupMemberDao = db.groupMemberDao()

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideGroupDao(db: AppDatabase): GroupDao = db.groupDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideGroupMemberRepository(
        dao: GroupMemberDao,
        groupDao: GroupDao,
        fs: FirebaseFirestore
    ): GroupMemberRepository =
        GroupMemberRepositoryImpl(dao, groupDao, fs)

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao, fs: FirebaseFirestore): NoteRepository =
        NoteRepositoryImpl(dao, fs)

    @Provides
    @Singleton
    fun provideGroupRepository(
        groupDao: GroupDao,
        groupMemberDao: GroupMemberDao,
        taskDao: TaskDao,
        noteDao: NoteDao,
        fs: FirebaseFirestore
    ): GroupRepository =
        GroupRepositoryImpl(groupDao, groupMemberDao, taskDao, noteDao, fs)

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao, fs: FirebaseFirestore): TaskRepository =
        TaskRepositoryImpl(dao, fs)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository =
        UserRepositoryImpl(dao)
}