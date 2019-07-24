package com.wz.kotlin_study

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by wangzhan on 2019-07-15 11:13
 */

class DataReceiveThread : Thread() {
    var isReceiving = true
    lateinit var ip: InetAddress
    var port: Int = 0


    fun setAddress(ip: InetAddress, port: Int) {
        this.ip = ip
        this.port = port
    }

    override fun run() {
        super.run()
        var da = DatagramSocket()
        var byte = byteArrayOf(

        )
        var datagramPacket = DatagramPacket(byte, byte.size)
        da.connect(ip, port)

        while (isReceiving) {

            da.receive(datagramPacket)

            println("---收到消息--" + datagramPacket.toString())

        }


    }

}