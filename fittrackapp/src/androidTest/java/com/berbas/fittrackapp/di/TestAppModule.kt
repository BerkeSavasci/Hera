package com.berbas.fittrackapp.di

import android.content.Context
import androidx.room.Room
import com.berbas.heraconnectcommon.localData.person.PersonDataBase
import com.berbas.heraconnectcommon.localData.sensor.FitnessDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_PersonDB")
    fun provideInMemoryPersonDb(@ApplicationContext context: Context) =
        // hold the db in ram
        Room.inMemoryDatabaseBuilder(context, PersonDataBase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    @Named("test_FitnessDB")
    fun provideInMemoryFitnessDb(@ApplicationContext context: Context) =
        // hold the db in ram
        Room.inMemoryDatabaseBuilder(context, FitnessDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}