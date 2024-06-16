package com.berbas.heraconnectcommon.connection

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.berbas.heraconnectcommon.protocolEngine.BluetoothProtocolEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {

    /**
     * Listens for incoming messages from the connected device
     * @return a flow of [ConnectionResult] that represents the result of the connection
     */
    fun listenForIncomingMessages(): Flow<ConnectionResult> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)

            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.d("BluetoothDataTransferService", "Socket closed, stopping reading from socket")
                    break
                }

                val receivedMessage = buffer.decodeToString(endIndex = byteCount)
                Log.d("BluetoothSyncViewModel", "Data received successfully: $receivedMessage")

                val protocolEngine = BluetoothProtocolEngine()

                emit(
                    protocolEngine.run {
                        ConnectionResult.TransferSuccess(
                            message = buffer.decodeToString(
                                endIndex = byteCount
                            ).toPersonDataMessage(
                                isFromMobile = true

                            )
                        )
                    }
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Sends a message to the connected device
     * @param bytes the message to send
     * @return true if the message was sent successfully, false otherwise
     */
    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch (e: IOException) {
                return@withContext false
            }
            true
        }
    }
}