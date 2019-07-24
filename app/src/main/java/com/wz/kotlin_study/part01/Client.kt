package com.wz.kotlin_study.part01

import java.net.InetAddress
import java.net.Socket

/**
 * Created by wangzhan on 2019-07-15 17:50
 */
class Client {
    lateinit var socket: Socket


    fun setIp(inetAddress: InetAddress) {
        socket = Socket(inetAddress, 50000)
    }

    fun sendData(byteArray: ByteArray) {
        var outputStream = socket.getOutputStream()
        outputStream.write(byteArray)
    }

    fun readData(): String {
        var inputStream = socket.getInputStream()
        return inputStream.read().toByte().toString()
    }

    fun stop() {
        socket.close()
    }

}