package com.berbas.hera

import android.content.Context
import android.widget.ArrayAdapter
import androidx.room.Room
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothDeviceDomain
import com.berbas.heraconnectcommon.localData.PersonDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {


    @Provides
    @Singleton
    fun provideBluetoothConnection(@ApplicationContext context: Context): BluetoothConnection {
        return BluetoothConnection(context)
    }

    @Provides
    @Singleton
    fun provideDevicesAdapter(@ApplicationContext context: Context): ArrayAdapter<BluetoothDeviceDomain> {
        return ArrayAdapter(context, android.R.layout.simple_list_item_1)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PersonDataBase {
        return Room.databaseBuilder(
            context,
            PersonDataBase::class.java, "database-name"
        ).build()
    }
}