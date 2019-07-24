package com.wz.kotlin_study

import java.net.ServerSocket
import java.net.Socket

/**
 * Created by wangzhan on 2019-07-15 17:34
 */

class Server {
    private var serverSocket: ServerSocket
    private lateinit var serverThread: Thread
    private lateinit var socket: Socket


    init {
        serverSocket = ServerSocket(50000, 10)
    }

    fun start() {
        serverThread = Thread(Runnable {
            socket = serverSocket.accept()
        })
        serverThread.start()
    }

    fun sendData(byteArray: ByteArray) {
        var outputStream = socket.getOutputStream()
        outputStream.write(byteArray)
    }

    fun readData(): String {
        ByteArray(1024)
        var inputStream = socket.getInputStream()
        return inputStream.read().toByte().toString()
    }


    fun stop() {
        serverSocket.close()
        serverThread.stop()
    }

}