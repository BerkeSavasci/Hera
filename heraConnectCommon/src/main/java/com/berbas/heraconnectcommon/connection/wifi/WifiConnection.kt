package com.berbas.heraconnectcommon.connection.wifi

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WifiConnection(
    private val urlWrapper: UrlWrapper
) : WifiConnectionInterface {

    /** The state of the wifi data transfer */
    private val _wifiState = MutableStateFlow(WifiState.IDLE)
    private val wifiState: StateFlow<WifiState> = _wifiState
    private val TAG = "WifiConnection"

    private var url = URL("http://5.28.100.60:3000/users")

    fun splitData(data: String, jsonObject: JSONObject): JSONObject {
        val dataSplit = data.substringAfter("Person(").substringBeforeLast(")").split(", ")

        for (item in dataSplit) {
            val keyValue = item.split("=")
            val key = keyValue[0]
            jsonObject.put(key, keyValue[1])
        }

        return jsonObject
    }

    override suspend fun send(data: String) {
        _wifiState.value = WifiState.IN_PROGRESS
        urlWrapper.setUrl(url)
        return withContext(Dispatchers.IO) {
            try {
                val jsonObject = splitData(data, JSONObject())

                Log.d("TAG", "Sending data to: ${urlWrapper.getUrl()}")
                val httpURLConnection = urlWrapper.openConnection().apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }

                httpURLConnection.outputStream.bufferedWriter().use {
                    it.write(jsonObject.toString())
                    it.flush()
                }

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK || httpURLConnection.responseCode == HttpURLConnection.HTTP_CREATED) {
                    Log.d("TAG", "Data sent successfully")
                    println("Data sent successfully")
                    _wifiState.value = WifiState.SUCCESS
                } else {
                    Log.d("TAG", "Failed to send data")
                    println("Failed to send data")
                    _wifiState.value = WifiState.FAILURE
                }
            } catch (e: Exception) {

                Log.d(TAG, "wifiState value before change: ${_wifiState.value}")
                _wifiState.value = WifiState.SERVER_ERROR
                Log.e("TAG", "Failed to send data", e)
                e.printStackTrace()
            }
        }
    }

    override suspend fun receive(id: Int): String {
        _wifiState.value = WifiState.IN_PROGRESS
        return withContext(Dispatchers.IO) {
            var result = ""
            try {
                val urlWithId = URL("$url/$id")
                urlWrapper.setUrl(urlWithId)
                Log.d(TAG, "Receiving data from: ${urlWrapper.getUrl()}")
                val httpURLConnection = urlWrapper.openConnection().apply {
                    requestMethod = "GET"
                    doInput = true
                    connectTimeout = 5000
                }

                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = httpURLConnection.inputStream.bufferedReader().use { it.readText() }
                    Log.d(TAG, "Data received successfully")
                    _wifiState.value = WifiState.SUCCESS
                } else {
                    _wifiState.value = WifiState.FAILURE
                }
            } catch (e: Exception) {
                _wifiState.value = WifiState.SERVER_ERROR
                Log.e(TAG, "Failed to receive data", e)
                e.printStackTrace()
            }
            result
        }
    }

    override fun getWifiState(): StateFlow<WifiState> = wifiState
}