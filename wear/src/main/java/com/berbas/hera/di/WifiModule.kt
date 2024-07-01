package com.berbas.hera.di

import android.content.Context
import com.berbas.heraconnectcommon.connection.wifi.UrlWrapper
import com.berbas.heraconnectcommon.connection.wifi.WifiConnection
import com.berbas.heraconnectcommon.connection.wifi.WifiConnectionInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WifiModule {

    @Singleton
    @Provides
    fun provideWifiController(@ApplicationContext context: Context): WifiConnectionInterface {
        val url = URL("http://5.28.100.60:3000/users")
        val urlWrapper = UrlWrapper(url)
        return WifiConnection(urlWrapper)
    }
}