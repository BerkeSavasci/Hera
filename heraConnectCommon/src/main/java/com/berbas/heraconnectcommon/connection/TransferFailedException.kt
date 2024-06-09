package com.berbas.heraconnectcommon.connection

import java.io.IOException

class TransferFailedException : IOException("Reading from the socket failed") {
}