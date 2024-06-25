package com.berbas.hera.di

import android.content.Context
import androidx.room.Room
import com.berbas.heraconnectcommon.localData.person.PersonDao
import com.berbas.heraconnectcommon.localData.person.PersonDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule{
    @Singleton
    @Provides
    fun providePersonDatabase(@ApplicationContext context: Context): PersonDataBase {
        return Room.databaseBuilder(
            context,
            PersonDataBase::class.java,
            "person.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providePersonDao(database: PersonDataBase): PersonDao {
        return database.dao
    }
}