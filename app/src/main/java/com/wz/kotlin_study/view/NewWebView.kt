package com.ceshui.mh.view

import android.content.Context
import android.util.AttributeSet
import com.github.lzyzsd.jsbridge.BridgeWebView

/**
 * Created by wangzhan on 2019-07-18 16:40
 */
class NewWebView : BridgeWebView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun overScrollBy(
        deltaX: Int,
        deltaY: Int,
        scrollX: Int,
        scrollY: Int,
        scrollRangeX: Int,
        scrollRangeY: Int,
        maxOverScrollX: Int,
        maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean {
        return false
    }


    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(0, 0)
    }
}