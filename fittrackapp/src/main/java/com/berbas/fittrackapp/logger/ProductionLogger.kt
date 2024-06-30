package com.berbas.fittrackapp.logger

import android.util.Log
import javax.inject.Inject

/** Is used to log the data for the production */
class ProductionLogger @Inject constructor() : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag,message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag,message)
    }

    override fun v(tag: String, message: String) {
        Log.v(tag,message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag,message)
    }
}