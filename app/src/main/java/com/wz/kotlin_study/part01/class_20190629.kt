package com.wz.kotlin_study.part01

/**
 * Created by wangzhan on 2019-06-28 10:39
 *
 *
 * 循环语句，if、when、while、for
 * break  continue
 *
 *
 *
 * while和do{}while 和java中的类似
 *
 *
 *
 *
 */



class class_20190629{

    fun foo() {
        listOf(1, 2, 3, 4, 5).forEach lit@{
            if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
            print(it)
        }
        print(" ---done with explicit label")
    }

}