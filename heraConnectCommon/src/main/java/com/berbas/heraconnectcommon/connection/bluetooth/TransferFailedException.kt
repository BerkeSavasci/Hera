package com.berbas.heraconnectcommon.connection.bluetooth

import java.io.IOException

class TransferFailedException : IOException("Reading from the socket failed") {
}