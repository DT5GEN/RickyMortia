package com.dt5gen.rickymortia.di

import android.content.Context
import androidx.room.Room
import com.dt5gen.rickymortia.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()
}
