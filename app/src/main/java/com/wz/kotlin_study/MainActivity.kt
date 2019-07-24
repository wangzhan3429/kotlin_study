package com.wz.kotlin_study

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.wz.kotlin_study.part01.ClassB
import com.wz.kotlin_study.part01.class_20190627
import com.wz.kotlin_study.part01.class_20190629
import com.wz.kotlin_study.part01.method
import com.wz.mylibrary.MyLibTest

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array.getLength
import android.content.res.AssetFileDescriptor
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.lzyzsd.jsbridge.BridgeWebView
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.github.lzyzsd.jsbridge.WebViewJavascriptBridge
import redis.clients.jedis.JedisPubSub
import java.io.OutputStream
import java.net.*


class MainActivity : AppCompatActivity() {

    lateinit var server: Server


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//

        val fileDescriptor = assets.openFd("city.mp3")

        var mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(
            fileDescriptor.getFileDescriptor(),
            fileDescriptor.getStartOffset(),
            fileDescriptor.getLength()
        )
        mediaPlayer.prepare()

        mediaPlayer.setOnCompletionListener {
            println("歌曲播放完毕。。。" + it.duration)
        }
        server = Server()
        var fibndService2 = FibndService2(this)
        fibndService2.initResolveListener()

        fab.setOnClickListener { view ->
            class_20190629().testFor()
            class_20190629().foo()
            method("wangzhan")
            ClassB().test()
            ClassB().innerClassB().g()
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()


            var a = MyLibTest()
            a.test()
//            R.string.test


//            mediaPlayer.start()
            fibndService2.registerService()
            bridge_webview.loadUrl("http://10.0.1.25:8001/testWeb/audioCicle.html")
            bridge_webview.send("lalal", object : CallBackFunction {
                override fun onCallBack(data: String?) {

                }
            })
//            bridge_webview.registerHandler()
//            initServer()
        }
        fibndService2.setOnServiceListener(object : FibndService2.OnServiceListener {
            override fun onService(info: NsdServiceInfo) {
                startToConnectDevice(info)
            }

        })

        fab_un.setOnClickListener {
            fibndService2.discoverService()



        }

        fab_send.setOnClickListener {
            Thread(Runnable {
                var da = Socket(ip, 50000)
                var out = da.getOutputStream()
                out.write("hello".toByteArray(charset("UTF-8")))

                var input = da.getInputStream()
                println("------" + input.readBytes().toString())

            }).start()
        }

        fab_receiv.setOnClickListener {
            //            initServer()
        }
    }

    lateinit var ip: InetAddress
    var port: Int = 50000


    // 查找到设备
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun startToConnectDevice(info: NsdServiceInfo) {
        this.ip = info.host
        this.port = 50000
//        var dataThread = DataReceiveThread()
//        dataThread.setAddress(ip, port)
//        dataThread.start()
    }

    fun initServer() {
        Thread(Runnable {
            var s = ServerSocket(50000, 10)
            var socket = s.accept()
            var byte = socket.getInputStream().readBytes()
            println("------收到客户端数据------" + byte.toString())
        }).start()
    }


    interface testCall : CallBackFunction {
        fun onCallBack(name: String, data: String?)
    }


    var ss: Any = object : testCall {
        override fun onCallBack(name: String, data: String?) {

        }

        override fun onCallBack(data: String?) {

        }
    }


    fun loadWebView() {
        var we = WebViewClient()


        webview.webViewClient = WebViewClient()
        webview.loadUrl("http://10.0.1.25:8001/testWeb/audioCicle.html")

        var settings = webview.getSettings()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false)
        }
    }

    fun test(){


    }

}
