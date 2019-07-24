package com.ceshui.mh.activity

import android.util.Log
import com.ceshui.mh.R
import com.ceshui.mh.event.NetEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by wangzhan on 2019-07-22 17:52
 */
class TestActivity : BaseActivity() {
    override fun getTitleName(): String? = ""

    override fun initActivity() {
    }

    override fun initView() {
        println("-------times" + Log.getStackTraceString(Throwable()))
    }

    override fun getLayoutId() = R.layout.activity_test


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceicerEvent(event: NetEvent) {

    }

}