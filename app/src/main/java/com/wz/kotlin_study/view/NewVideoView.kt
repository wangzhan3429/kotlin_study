package com.ceshui.mh.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView
import com.ceshui.mh.utils.KLog
import com.ceshui.mh.utils.StatusNavUtils

/**
 * Created by wangzhan on 2019-07-16 14:38
 */
class NewVideoView : VideoView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = View.getDefaultSize(0, widthMeasureSpec)
        var height = View.getDefaultSize(0, heightMeasureSpec)
        height += StatusNavUtils.getStatusBarHeight(context) * 2
        KLog.d("height======" + height)
        setMeasuredDimension(width, height)
    }
}