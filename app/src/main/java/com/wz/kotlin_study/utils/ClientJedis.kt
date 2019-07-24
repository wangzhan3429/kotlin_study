package com.ceshui.mh.utils

import com.ceshui.mh.Const
import com.ceshui.mh.event.NetEvent
import org.greenrobot.eventbus.EventBus
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool as JedisPool1
import redis.clients.jedis.JedisPool
import java.util.concurrent.ConcurrentHashMap


/**
 * Created by wangzhan on 2019-07-16 17:54
 */

class ClientJedis { // JEDIS 对象是否需要新的
    private lateinit var jedis: Jedis
    private var connected: Boolean = true
    private val maps = ConcurrentHashMap<String, JedisPool>()


    //Redis的端口号
    private val PORT = 6379


    fun connect(ip: String, reTry: Boolean) { // 192.168.1.103
        KLog.d("----connect redis ip---" + ip)
        try {
            jedis = Jedis(ip, PORT)
            jedis.connect()
            KLog.d("----waiting connect---" + ip)
        } catch (e: Exception) {
            jedis.close()
            connected = false
            EventBus.getDefault().post(NetEvent(Const.CLIENT_CONNECT_FAILED, reTry))
            KLog.d("------connect failed---" + ip) // 异常的地方，try catch，然后重新访问网络，请求新的ip地址
        } finally {
            if (connected) {
                KLog.d("-----subscribe-----")
                EventBus.getDefault().post(NetEvent(Const.CLIENT_CONNECT_SUCCEED,ip))
                subscribe()
            }
        }
    }

    // 客户端订阅服务端  接收服务端消息， 发送消息在 ServerJedis
    fun subscribe() {
        try {
            var subscriber = ClientSubscriber()
            jedis.subscribe(subscriber, Const.PC)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //  客户端publish消息
    fun publish(message: String) {
        jedis.publish(Const.PAD, message)
    }


//    /**
//     * 获取连接池.
//     */
//    private fun getPool(ip: String): JedisPool? {
//        val key = ip + PORT
//        var pool: JedisPool? = null
//        if (!maps.containsKey(key)) {//根据ip和端口判断连接池是否存在.
//            val config = JedisPoolConfig()
//            config.maxTotal = MAX_ACTIVE
//            config.maxIdle = MAX_IDLE
//            config.maxWaitMillis = MAX_WAIT.toLong()
//            config.testOnBorrow = TEST_ON_BORROW
//            config.testOnReturn = TEST_ON_RETURN
//            try {
//                pool = JedisPool(config, ip)
//
//                maps.put(key, pool)
//            } catch (e: Exception) {
//                KLog.e("初始化Redis连接池异常:", e)
//            }
//        } else {
//            pool = maps.get(key)
//        }
//        return pool
//    }


}