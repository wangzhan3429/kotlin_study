package com.ceshui.mh.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.Toast
import com.ceshui.mh.utils.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by wangzhan on 2019-07-16 10:55
 */

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var context: BaseActivity
    protected var self_control_back = false//是否自主控制返回按钮事件
    protected var isInterruptProcess = false//是否中断执行
    protected var isSupportBle = true
    private var loadDialog: Dialog? = null//loading mAutoDismissDialog
    protected var isNeedSetStatusBar = true//是否需要设置status
    protected var isSelfControlEvent = false//是否子类控制EventBus
    protected var isNeedWiFiOrBluetoothTip = true//是否需要监控蓝牙和WiFi

    //region 生命周期

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ActManager.addActivity(context)

        beforeSetContentView()

        if (getLayoutId() != -1) {
            if (getLayoutId() == 0) {
                throw RuntimeException("Layout files can not be empty")
            }
            setContentView(getLayoutId())
            if (isNeedSetStatusBar) {
                StatusNavUtils.setStatusNavBarTransparent(this)
            }
        }

        initActivity()

        if (isInterruptProcess) return

//        if (getTitleName() != null) {
//            if (aiv_activity_back != null) {
//                aiv_activity_back.setOnClickListener {
//                    if (!self_control_back) onBackClick()
//                }
//            }
//            if ("" != getTitleName() && tv_activity_title != null) {
//                tv_activity_title.text = getTitleName()
//            }
//        }

        initView()
    }

//    override fun getResources(): Resources {
//        return ScreenUtil.adaptWidth(super.getResources(), 720)
//    }

    override fun onResume() {
        super.onResume()
        if (!isSelfControlEvent) {
            EventBus.getDefault().register(this)
        }

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    override fun onPause() {
        if (!isSelfControlEvent) {
            EventBus.getDefault().unregister(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        println("-------ondestory")
        ActManager.removeActivity(context)
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            onBackClick()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //endregion

    //region 公共方法
    fun redirectNoParamAndFinish(cls: Class<*>) {
        redirectNoParam(cls)
        context.finish()
    }

    fun redirectWithExtraAndFinish(param: Int, cls: Class<*>) {
        redirectWithExtra(param, cls)
        context.finish()
    }

    fun redirectNoParam(cls: Class<*>) {
        startActivity(Intent(context, cls))
    }

    fun redirectWithExtra(param: Any, cls: Class<*>) {
        val intent = Intent(context, cls)
        when (param) {
//            is Int -> intent.putExtra(Const.EXTRA_DATA, param)
//            is String -> intent.putExtra(Const.EXTRA_DATA, param)
//            is Boolean -> intent.putExtra(Const.EXTRA_DATA, param)
        }
        startActivity(intent)
    }
    //endregion

    //region Toast || Dialog

    fun showToast(txt: String) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show()
    }

    fun showToast(txt: CharSequence) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes txtId: Int) {
        Toast.makeText(context, getString(txtId), Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(txt: String) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show()
    }

//    /**
//     * 弹出加载框
//     */
//    @SuppressLint("InflateParams")
//    fun showLoadingDialog() {
//        if (loadDialog == null) {
//            loadDialog = Dialog(context, R.style.CommonDialogStyle)
//            val view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null, false)
//            loadDialog!!.setContentView(view)
//            loadDialog!!.setCancelable(true)
//            loadDialog!!.setCanceledOnTouchOutside(false)
//        }
//        if (!context.isFinishing && !loadDialog!!.isShowing) {
//            loadDialog!!.show()
//        }
//    }

    /**
     * 隐藏加载框
     */
    fun hideLoadingDialog() {
        if (loadDialog != null && loadDialog!!.isShowing) {
            loadDialog!!.dismiss()
        }
    }
    //endregion

    //region //蓝牙和WiFi断开状态监听

//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 999)
//    fun onBaseReceiverEvent(event: NetEvent) {
//        if (!isNeedWiFiOrBluetoothTip) return
//        if (event.type != Const.BLUETOOTH_OFF) return
//        showBluetoothOffDialog()
//    }

    //endregion

    //region 蓝牙离线弹窗
//
//    private fun showBluetoothOffDialog() {
//        if (bleDialog == null) {
//            bleDialog = CommonDialog.Builder(context, 2)
//                .setTitle(getString(R.string.dialog_open_bluetooth))
//                .setContent(getString(R.string.dialog_open_bluetooth_tip))
//                .create()
//        }
//        if (bleDialog != null && !bleDialog!!.isShowing) {
//            bleDialog!!.show()
//        }
//    }

    //endregion


//    fun isFirstLauncher(): Boolean {
//        return SpUtil.get(Const.IS_FIRST_LAUNCHER, true)
//    }


    //region 抽象方法--需继承

    protected open fun beforeSetContentView() {

    }

    /**
     * 获取布局文件
     *
     * @return -1:不设置布局,0:布局文件不合法抛出异常
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 获取title名称
     * title为null时不设置返回和title,title为空时,只设置返回监听
     *
     * @return Activity Title文本
     */
    protected abstract fun getTitleName(): String?

    /**
     * 初始化Activity相关参数和变量,和view无关
     */
    protected abstract fun initActivity()

    /**
     * 初始化View控件
     */
    protected abstract fun initView()

    /**
     * 返回键监听
     */
    protected open fun onBackClick() {
        context.finish()
    }
    //endregion
}