package com.berbas.fittrackapp.screens.connections.bluetooth

import android.content.Context
import com.berbas.heraconnectcommon.connection.BluetoothConnection
import com.berbas.heraconnectcommon.connection.BluetoothControllerInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Singleton
    @Provides
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothControllerInterface {
        return BluetoothConnection(context)
    }
}