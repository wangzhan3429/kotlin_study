package com.ceshui.mh.utils

import android.util.Log
import com.ceshui.mh.BuildConfig

/**
 * 本地打印日志
 *
 * wangzhan@mirahome.me
 */
object KLog {
    private const val customTagPrefix: String = "k_log"
    private const val LOG_DEBUG = BuildConfig.LOG_DEBUG

    private fun generateTag(): String {
        val caller = Throwable().stackTrace[2]
        var tag = "%s.%s(L:%d)"
        var callerClazzName = caller.className
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
        tag = String.format(tag, callerClazzName, caller.methodName, caller.lineNumber)
        tag = if (customTagPrefix.isEmpty()) tag else "$customTagPrefix:$tag"
        return tag
    }

    fun d(content: String) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.d(tag, content)
    }

    fun d(content: String, tr: Throwable) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.d(tag, content, tr)
    }

    fun e(content: String) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.e(tag, content)
    }

    fun e(content: String, tr: Throwable) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.e(tag, content, tr)
    }

    fun i(content: String) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.i(tag, content)
    }

    fun i(content: String, tr: Throwable) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.i(tag, content, tr)
    }

    fun v(content: String) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.v(tag, content)
    }

    fun v(content: String, tr: Throwable) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.v(tag, content, tr)
    }

    fun w(content: String) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.w(tag, content)
    }

    fun w(content: String, tr: Throwable) {
        if (!LOG_DEBUG) return
        val tag = generateTag()
        Log.w(tag, content, tr)
    }
}