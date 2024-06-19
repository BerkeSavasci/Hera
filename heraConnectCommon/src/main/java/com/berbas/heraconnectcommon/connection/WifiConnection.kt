package com.berbas.heraconnectcommon.connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class WifiConnection: WifiConnectionInterface {

    override fun send(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonArray = JSONArray()
                val dataSplit = data.split(",")

                for (item in dataSplit) {
                    val jsonObject = JSONObject()
                    jsonObject.put("name", item)
                    jsonArray.put(jsonObject)
                }

                val url = URL("http://192.168.178.73/users")
                val httpURLConnection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }

                httpURLConnection.outputStream.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(jsonArray.toString())
                        writer.flush()
                    }
                }

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    println("Data sent successfully")
                }
                else {
                    println("Failed to send data")
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun receive(): String {
        TODO("Not yet implemented")
    }

}