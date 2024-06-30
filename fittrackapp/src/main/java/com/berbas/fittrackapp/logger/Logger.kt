package com.berbas.fittrackapp.logger

interface Logger {
    fun d(tag:String, message: String)
    fun i(tag:String, message: String)
    fun v(tag:String, message: String)
    fun e(tag:String, message: String)
}