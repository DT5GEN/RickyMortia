package com.dt5gen.rickymortia.di

import com.dt5gen.rickymortia.data.RickyMortiaRepository
import com.dt5gen.rickymortia.data.RickyMortiaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindRickyMortiaRepository(
        impl: RickyMortiaRepositoryImpl
    ): RickyMortiaRepository
}