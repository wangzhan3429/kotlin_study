package com.ceshui.mh.utils

import android.content.SharedPreferences

/**
 *
 *
 * Created by ZapFive on 2019-05-25.
 *
 * wuzhuang@mirahome.me
 */
object SpUtil {

    lateinit var sp: SharedPreferences

    /**
     * SharedPreferences 存入值
     *
     * @param key   key值
     * @param value 存入值
     */
    fun put(key: String, value: Any) {
        val editor = sp.edit()
        if (key.isEmpty()) return
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
        }
        editor.apply()
    }

    operator fun get(key: String, def: Int): Int {
        return sp.getInt(key, def)
    }

    operator fun get(key: String, def: String = ""): String {
        return sp.getString(key, def)!!
    }

    operator fun get(key: String, def: Boolean): Boolean {
        return sp.getBoolean(key, def)
    }

    operator fun get(key: String, def: Long): Long {
        return sp.getLong(key, def)
    }

    operator fun get(key: String, def: Float): Float {
        return sp.getFloat(key, def)
    }

    operator fun get(key: String): HashSet<String> {
        return sp.getStringSet(key, HashSet()) as HashSet<String>
    }


}