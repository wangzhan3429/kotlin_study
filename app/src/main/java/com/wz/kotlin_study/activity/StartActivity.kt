package com.ceshui.mh.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import com.ceshui.mh.AppContext
import com.ceshui.mh.Const.CLIENT_RECEIVE_MSG_PC
import com.ceshui.mh.event.NetEvent
import kotlinx.android.synthetic.main.activity_start.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.ceshui.mh.R
import com.ceshui.mh.event.MsgEvent
import com.ceshui.mh.utils.ClientObj
import com.ceshui.mh.utils.KLog


class StartActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_start
    }

    override fun getTitleName(): String? {
        return ""
    }

    @SuppressLint("JavascriptInterface")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun initActivity() {
        // android 调用js
        btn_js.setOnClickListener {
            var str =
                "Xinhua News Agency, Chengdu, July 21 (Reporters Chen Fei and Xiong Feng) The Conference on the promotion of comprehensive deepening reform in the field of politics and law was held in Chengdu a few days ago. Reporters learned from the meeting that since 2018, the reform of the political and legal institutions, the comprehensive reform of the judicial system and the reform of various units of the political and legal system have been carried out as a whole. Eight of the key reform tasks have been completed, 28 are being carried out and two are being studied in depth. Reform in the field of politics and law has entered a new stage of systematic and holistic reform."
            webview.evaluateJavascript("javascript:androidCallWeb('" + str + "')", object : ValueCallback<String> {
                override fun onReceiveValue(value: String?) {
//                    showToast("receive msg ===" + value)
                    KLog.d("actionToJs---" + value)
                }
            })
        }
    }

    // pc 来的消息传递给 js
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun actionToJs(str: String) {
        webview.evaluateJavascript("javascript:androidCallWeb('" + str + "')", object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                KLog.d("actionToJs---" + value)
            }
        })
    }

    // js来的消息传递给 pc
    @android.webkit.JavascriptInterface
    fun actionFromJs(message: String) {
        KLog.d("---actionFromJs---" + message)
        Thread(Runnable {
            ClientObj.getServerJ().publish("message from js--" + message)
        }).start()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun initView() {
        KLog.d("----INIT START---")

        var url = intent.getStringExtra("url")

        val webSettings = webview.settings
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webSettings.javaScriptEnabled = true
        webview.setInitialScale(75)
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webview.webViewClient = WebViewClient()
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webview.webChromeClient = WebChromeClient()
        webview.clearCache(true)
        webview.addJavascriptInterface(this, "ceshuibao")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.settings.mediaPlaybackRequiresUserGesture = false
        }
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webSettings.domStorageEnabled = true // 打开本地缓存提供JS调用,至关重要
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)
        var appCachePath = application.cacheDir.absolutePath
        webSettings.setAppCachePath(appCachePath)
        webSettings.databaseEnabled = true

        webview.loadUrl(url)


    }

//     fun beforeSetContentView() {
////        isNeedSetStatusBar = false
////        isNeedWiFiOrBluetoothTip = false
//
//        var flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            flag = flag or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        }
//        window.decorView.systemUiVisibility = flag
//    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceicerEvent(event: MsgEvent) {
        if (event.type == CLIENT_RECEIVE_MSG_PC) { // 从pc来的消息
            actionToJs(event.msg)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}
