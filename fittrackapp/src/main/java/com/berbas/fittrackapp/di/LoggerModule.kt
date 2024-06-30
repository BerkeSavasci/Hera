package com.berbas.fittrackapp.di


import com.berbas.fittrackapp.logger.Logger
import com.berbas.fittrackapp.logger.ProductionLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {

    @Binds
    abstract fun bindLogger(productionLogger: ProductionLogger): Logger
}
