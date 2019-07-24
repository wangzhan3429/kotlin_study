package com.ceshui.mh.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.ceshui.mh.BuildConfig
import kotlinx.coroutines.Job
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 公工工具类
 *
 * Created by ZapFive on 2019-05-25.
 *
 * wuzhuang@mirahome.me
 */
object MUtil {

    /**
     * 获取请求公共头部User-Agent标志
     */
    fun getUserAgent(): String {
        return "os:Android,os_version:${Build.VERSION.RELEASE},phone_brand:${Build.MODEL},app_name:${BuildConfig.APP_NAME},app_version:${BuildConfig.VERSION_NAME}"
    }

    /**
     * 强制隐藏输入法键盘
     */
    fun hideInputMethod(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 停止Job
     */
    fun cancelJob(job: Job?) {
        if (job != null && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 获取距离开始时间的时间长度
     *
     * @param startHour  开始时间18点
     * @param timeString 21:30
     * @param pattern    HH:mm
     * @return 时长
     */
    fun getTimeDuration(startHour: Int, timeString: String, pattern: String): Int {
        val cal = Calendar.getInstance()
        var duration = 0
        try {
            cal.time = SimpleDateFormat(pattern, Locale.getDefault()).parse(timeString)
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            if (hour < startHour) hour += 24
            duration = cal.get(Calendar.MINUTE) * 60 + (hour - startHour) * 3600
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return duration
    }

    /**
     * 根据时间格式获取小时和分钟
     *
     * @param timeString 21:30
     * @param pattern    HH:mm
     * @return 小时+分钟
     */
    fun getHourAndMinute(timeString: String, pattern: String): IntArray {
        val cal = Calendar.getInstance()
        try {
            cal.time = SimpleDateFormat(pattern, Locale.getDefault()).parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return intArrayOf(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    }

    /**
     * 解析日期中的年、月、日
     *
     * @param date 2017-07-20
     * @return 2017、7、21
     */
    fun getYearMonthDay(date: String): IntArray {
        val cal = Calendar.getInstance()
        try {
            cal.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return intArrayOf(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
    }
}