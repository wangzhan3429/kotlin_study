package com.ceshui.mh.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.model.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Json解析
 *
 * Created by ZapFive on 2019-05-25.
 *
 * wuzhuang@mirahome.me
 */
abstract class JsonCallback<T> : AbsCallback<T> {

    private var type: Type? = null
    private var clazz: Class<T>? = null
    private lateinit var gson: Gson

    constructor(type: Type) {
        this.type = type
        initGson()
    }

    constructor(clazz: Class<T>) {
        this.clazz = clazz
        initGson()
    }

    constructor() {
        initGson()
    }

    private fun initGson() {
        gson = GsonBuilder().serializeNulls().create()
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     */
    @Throws(Throwable::class)
    override fun convertResponse(response: okhttp3.Response): T? {
        return when {
            clazz != null -> gson.fromJson(response.body()?.string(), clazz)
            type != null -> gson.fromJson<T>(response.body()?.string(), type)
            else -> {
                val genType = javaClass.genericSuperclass
                val type = (genType as ParameterizedType).actualTypeArguments[0]
                gson.fromJson<T>(response.body()?.string(), type)
            }
        }
    }

    override fun onSuccess(response: Response<T>?) {
        if (response != null && response.isSuccessful && response.body() != null) {
            onNormalSuccess(response)
        } else {
            onError(response)
        }
    }

    abstract fun onNormalSuccess(response: Response<T>)
}