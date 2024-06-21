package com.berbas.heraconnectcommon.connection

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WifiConnection : WifiConnectionInterface {
    private var url = URL("http://5.28.100.60:3000/users")

    override fun send(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonObject = JSONObject()
                val dataSplit = data.substringAfter("Person(").substringBeforeLast(")").split(", ")

                for (item in dataSplit) {
                    val keyValue = item.split("=")
                    val key = keyValue[0]
                    jsonObject.put(key, keyValue[1])
                }

                Log.d("WifiConnection", "Sending data to: $url")
                val httpURLConnection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }

                httpURLConnection.outputStream.bufferedWriter().use {
                    it.write(jsonObject.toString())
                }

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK || httpURLConnection.responseCode == HttpURLConnection.HTTP_CREATED) {
                    Log.d("WifiConnection", "Data sent successfully")
                    println("Data sent successfully")
                } else {
                    Log.d("WifiConnection", "Failed to send data")
                    println("Failed to send data")
                }
            } catch (e: Exception) {
                Log.e("WiFiConnection", "Failed to send data", e)
                e.printStackTrace()
            }
        }
    }

    override suspend fun receive(id: Int): String {
        return withContext(Dispatchers.IO) {
            var result = ""
            try {
                val urlWithId = URL("$url/$id")
                Log.d("WifiConnection", "Receiving data from: $urlWithId")
                val httpURLConnection = (urlWithId.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    doInput = true
                }

                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = httpURLConnection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("WifiConnection", "Data received successfully")
                    println("Data received successfully")
                } else {
                    println("Failed to receive data")
                }
            } catch (e: Exception) {
                Log.e("WiFiConnection", "Failed to receive data", e)
                e.printStackTrace()
            }
            result
        }
    }
}