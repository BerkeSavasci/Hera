package com.berbas.heraconnectcommon.connection

import java.net.HttpURLConnection
import java.net.URL

open class UrlWrapper(private var url: URL) {
    fun setUrl(url: URL) {
        this.url = url
    }

    fun getUrl(): URL {
        return url
    }

    open fun openConnection(): HttpURLConnection {
        return url.openConnection() as HttpURLConnection
    }
}