package com.berbas.hera.di

import com.berbas.hera.annotations.UserId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing the user id
 * for now only one user is supported
 */

@Module
@InstallIn(SingletonComponent::class)
object UserIdModule {

    @Singleton
    @Provides
    @UserId
    fun provideUserId(): Int {
        return 1
    }
}