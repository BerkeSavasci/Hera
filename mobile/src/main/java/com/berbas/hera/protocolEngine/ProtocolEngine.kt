package com.berbas.hera.protocolEngine

interface ProtocolEngine {
    fun serialize(data: Any): ByteArray
    fun deserialize(data: ByteArray): Any
}