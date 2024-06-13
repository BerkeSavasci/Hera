package com.berbas.fittrackapp.data

import android.content.Context
import androidx.room.Room
import com.berbas.heraconnectcommon.localData.PersonDao
import com.berbas.heraconnectcommon.localData.PersonDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing the room database
 *
 * Hilt automatically creates and provides the dependencies declared in RoomDatabaseModule wherever
 * they're needed in your application, as long as the classes where they're used are also
 * part of the Hilt dependency injection system
 * (annotated with @HiltAndroidApp, @AndroidEntryPoint, etc.).
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    /**
     * Provides the PersonDataBase
     *
     * the fallbackToDestructiveMigration()(kann später enfernt werden) method is used to recreate the database
     * if there is a new version (kb mit dem migration)
     */
    @Singleton
    @Provides
    fun providePersonDatabase(@ApplicationContext context: Context): PersonDataBase {
        return Room.databaseBuilder(
            context,
            PersonDataBase::class.java,
            "person.db"
        ).fallbackToDestructiveMigration().build(
        )
    }

    /**
     * Provides the PersonDao
     */
    @Singleton
    @Provides
    fun providePersonDao(database: PersonDataBase): PersonDao {
        return database.dao
    }

}