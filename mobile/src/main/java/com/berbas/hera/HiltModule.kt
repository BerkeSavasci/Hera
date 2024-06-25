package com.berbas.hera

/*
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
 */