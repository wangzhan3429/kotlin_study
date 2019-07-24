package com.ceshui.mh.activity

import android.content.Intent
import com.ceshui.mh.Const
import com.ceshui.mh.R
import com.ceshui.mh.entry.IpBean
import com.ceshui.mh.event.NetEvent
import com.ceshui.mh.utils.*
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by wangzhan on 2019-07-16 18:28
 */
class MainActivity : BaseActivity() {

    lateinit var ipData: List<IpBean.Data>
    var curIndex = 0
    var maxRetry = 0 // ip连接失败，重试次数
    private lateinit var clientJedis: ClientJedis

    override fun initView() {
        curIndex = 0
        maxRetry = 0
        KLog.d("---INIT----")

        btn_url.setOnClickListener {
            var intent = Intent(this, StartActivity::class.java)
            intent.putExtra("url", et.text.toString())
            startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getTitleName() = ""

    override fun initActivity() {
        getUrl()
    }

    private fun getUrl() {
        KLog.d("----geturl-----")
        if (maxRetry > Const.MAX_RETRY) {
            KLog.e("----max retry---")
            return
        }
        maxRetry++
        OkGo.get<IpBean>(Const.GET_IP).tag(this).execute(object : JsonCallback<IpBean>() {

            override fun onNormalSuccess(response: Response<IpBean>) {
                if (response.body().error.isEmpty()) {
                    ipData = response.body().data!!
                    parseIp(ipData)
                } else {
                    getUrl()
                    KLog.e("----get ip failed---")
                }
            }

            override fun onError(response: Response<IpBean>?) {
                super.onError(response)
                KLog.e("response: $response")
            }

            override fun onFinish() {
                hideLoadingDialog()
            }
        })
    }

    private fun parseIp(data: List<IpBean.Data>?) {
        Thread(Runnable {
            KLog.d("----thread connect---")
            clientJedis = ClientJedis()
            clientJedis.connect(data!![curIndex].private_ip, false)

        }).start()
    }


    // 思考 加上 getUrl 最大次数，超过最大次数直接提示获取ip地址失败，可能电视还没开机
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceicerEvent(event: NetEvent) {
        KLog.d("----event---type==" + event.type)
        if (event.type == Const.CLIENT_CONNECT_FAILED) {
            if (event.retry) {
                getUrl()
            } else {
                if (curIndex < ipData.size - 1) {
                    curIndex++
                    parseIp(ipData)
                } else {
                    getUrl()
                }

            }
        } else if (event.type == Const.CLIENT_CONNECT_SUCCEED) {
            KLog.d("----connect succeed--")
            ClientObj.setClientJ(clientJedis)

            Thread(Runnable {
                var ip = event.ip
                var serverJedis = ServerJedis()
                serverJedis.connect(ip)
                ClientObj.setServerJ(serverJedis)
            }).start()

        }
    }

}