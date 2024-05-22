package com.example.heraconnect.protocolEngine

interface ProtocolEngine {
    fun serialize(data: Any): ByteArray
    fun deserialize(data: ByteArray): Any
}