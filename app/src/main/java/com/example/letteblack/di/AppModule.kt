package com.example.letteblack.di

import android.content.Context
import androidx.room.Room
import com.example.letteblack.db.AppDatabase
import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.NoteDao
import com.example.letteblack.repositories.GroupMemberRepository
import com.example.letteblack.repositories.GroupMemberRepositoryImpl
import com.example.letteblack.repositories.GroupRepository
import com.example.letteblack.repositories.GroupRepositoryImpl
import com.example.letteblack.repositories.NoteRepository
import com.example.letteblack.repositories.NoteRepositoryImpl
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
    @Singleton
    fun provideGroupMemberRepository(dao: GroupMemberDao, gd: GroupDao,fs: FirebaseFirestore): GroupMemberRepository =
        GroupMemberRepositoryImpl(dao,gd, fs)

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao, fs: FirebaseFirestore): NoteRepository =
        NoteRepositoryImpl(dao, fs)

    @Provides fun provideGroupDao(db: AppDatabase): GroupDao = db.groupDao()

    @Provides @Singleton
    fun provideGroupRepository(
        dao: GroupDao,
        memberDao: GroupMemberDao,
        fs: FirebaseFirestore
    ): GroupRepository = GroupRepositoryImpl(dao, memberDao, fs)

}
