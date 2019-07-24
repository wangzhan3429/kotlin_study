package com.wz.kotlin_study

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.ServerSocket

/**
 * Created by wangzhan on 2019-07-15 11:13
 */

class DataSendThread : Thread() {
    var isReceiving = true
    lateinit var ip: InetAddress
    var port: Int = 0

    override fun run() {
        super.run()
        var da = ServerSocket()
        var byte = byteArrayOf()


//        while (isReceiving) {


//            println("---收到消息--" + datagramPacket.toString())

//        }


    }

}