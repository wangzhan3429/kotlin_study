package com.ceshui.mh.utils

import android.app.Activity
import java.util.*

/**
 * Activity管理
 *
 * wuzhuang@mirahome.me
 */
object ActManager {

    private val actStack = Stack<Activity>()

    /**
     * 向集合中添加指定Activity
     */
    fun addActivity(act: Activity?) {
        if (act == null) return
        actStack.add(act)
    }

    /**
     * 从集合中移除指定Activity
     */
    fun removeActivity(act: Activity?) {
        if (act == null) return
        actStack.remove(act)
    }

    /**
     * 查找指定Activity
     */
    fun findActivity(cls: Class<*>): Activity? {
        if (actStack.empty()) return null
        for (act in actStack) {
            if (act.javaClass == cls) {
                return act
            }
        }
        return null
    }

    /**
     * 是否存在指定Activity
     */
    fun existActivity(cls: Class<*>): Boolean {
        if (actStack.empty()) return false
        for (act in actStack) {
            if (act.javaClass == cls) return true
        }
        return false
    }

    /**
     * 回到第一个Activity
     */
    fun redirectToBottomActivity() {
        if (actStack.empty()) return
        for (i in 1 until actStack.size) {
            if (actStack[i] != null && !actStack[i].isFinishing) {
                KLog.d("pkgName  -- " + actStack[i].packageName + "\n actName  --  " + actStack[i].componentName.className)
                actStack[i].finish()
            }
        }
    }

    /**
     * 结束全部当前栈中
     */
    fun finishAll() {
        if (actStack.empty()) return
        for (act in actStack) {
            if (act != null && !act.isFinishing) {
                KLog.d("pkgName  -- " + act.packageName + "\n actName  --  " + act.componentName.className)
                act.finish()
            }
        }
        actStack.clear()
    }

    /**
     * 结束当前以上的所有Activity
     */
    fun finishAbove(cls: Class<*>) {
        if (actStack.empty()) return
        for (i in 0 until actStack.size) {
            if (actStack[i].javaClass == cls) {
                if (i == actStack.size - 1) return
                for (j in i + 1 until actStack.size) {
                    val act = actStack[j]
                    if (act != null && !act.isFinishing) act.finish()
                }
                return
            }
        }
    }

    /**
     * 除了指定Activity结束
     */
    fun finishExcept(cls: Class<*>) {
        if (actStack.empty()) return
        for (i in 0 until actStack.size) {
            if (actStack[i].javaClass != cls) {
                val act = actStack[i]
                if (act != null && !act.isFinishing) act.finish()
            }
        }
    }

}