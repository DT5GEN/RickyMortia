package com.dt5gen.rickymortia.di

import com.dt5gen.rickymortia.data.GreetingRepository
import com.dt5gen.rickymortia.data.GreetingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGreetingRepository(): GreetingRepository = GreetingRepositoryImpl()
}
