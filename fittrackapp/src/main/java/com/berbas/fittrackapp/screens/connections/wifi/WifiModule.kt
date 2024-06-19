package com.berbas.fittrackapp.screens.connections.wifi

import android.content.Context
import com.berbas.heraconnectcommon.connection.WifiConnectionInterface
import com.berbas.heraconnectcommon.connection.WifiConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WifiModule {

    @Singleton
    @Provides
    fun provideWifiController(@ApplicationContext context: Context): WifiConnectionInterface {
        return WifiConnection()
    }
}