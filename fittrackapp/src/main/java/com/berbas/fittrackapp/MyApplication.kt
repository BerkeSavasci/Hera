package com.berbas.fittrackapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class that contains global application state.
 * This class is used to initialize the application.
 * Use it to store global configuration data,
 * initialize singleton objects, set up specific settings, etc.
 */

@HiltAndroidApp
class MyApplication: Application() {

}