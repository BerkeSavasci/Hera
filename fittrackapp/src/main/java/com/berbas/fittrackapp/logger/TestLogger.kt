package com.berbas.fittrackapp.logger

/** A test logger to be able to tests classes that contain logs */
class TestLogger : Logger {
    override fun d(tag: String, message: String) {
        println("$tag: $message")
    }

    override fun i(tag: String, message: String) {
        println("$tag: $message")
    }

    override fun v(tag: String, message: String) {
        println("$tag: $message")
    }

    override fun e(tag: String, message: String) {
        println("$tag: $message")
    }
}