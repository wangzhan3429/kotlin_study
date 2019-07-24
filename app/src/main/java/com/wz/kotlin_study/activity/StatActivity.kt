package com.ceshui.mh.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.ceshui.mh.R
import kotlinx.android.synthetic.main.activity_start.*

/**
 * Created by wangzhan on 2019-07-23 15:36
 */
class StatActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()
        initActivity()
    }

    @SuppressLint("JavascriptInterface")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun initActivity() {
        // android 调用js
        btn_js.setOnClickListener {
            var str =
                "Xinhua News Agency, Chengdu, July 21 (Reporters Chen Fei and Xiong Feng) The Conference on the promotion of comprehensive deepening reform in the field of politics and law was held in Chengdu a few days ago. Reporters learned from the meeting that since 2018, the reform of the political and legal institutions, the comprehensive reform of the judicial system and the reform of various units of the political and legal system have been carried out as a whole. Eight of the key reform tasks have been completed, 28 are being carried out and two are being studied in depth. Reform in the field of politics and law has entered a new stage of systematic and holistic reform."
            webview.evaluateJavascript("javascript:androidCallWeb('" + str + "')", object : ValueCallback<String> {
                override fun onReceiveValue(value: String?) {
//                    showToast("receive msg ===" + value)
                }
            })
        }
    }

    @android.webkit.JavascriptInterface
    fun actionFromJs(name: String) {
        runOnUiThread {
            //            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun initView() {
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
}