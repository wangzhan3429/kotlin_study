package com.wz.kotlin_study.part01

import java.util.*

/**
 * Created by wangzhan on 2019-07-10 10:39
 */
class ObjectTest {


    // 对象声明  在kotlin中，单例是用对象声明的形式实现
    object obj {
        fun testA() {

        }

        fun testB() {

        }
    }

    fun objTest() {
        obj.testA()
    }


    interface A {
        fun testA()
    }

    interface B {
        fun testB()
    }

    // 匿名内部类
    var ss: Any = object : A, B {
        override fun testB() {
            Test.createFactory() // 伴生对象，可以直接调用伴生对象中的方法
            var s = TestA // 直接调用伴生对象
        }

        override fun testA() {

        }
    }

    class Test {
        companion object Factory { // companion 伴生对象
            fun createFactory() {

            }
        }
    }

    class TestA {
        companion object {}
    }

//    typealias namedAdd<Int> = (Int, Int) -> Int

//    typealias Predicate<T> = (T) -> Boolean
//
//    fun foo(p: Predicate<Int>) = p(42)
//
//    fun main() {
//        val f: (Int) -> Boolean = { it > 0 }
//        println(foo(f)) // 输出 "true"
//
//        val p: Predicate<Int> = { it > 0 }
//        println(listOf(1, -2).filter(p)) // 输出 "[1]"
//    }


    /*
    *
    *  kotlin 集合分为可变和不可变集合，可变集合可以增删改元素，不可变集合只能读数据
    *  HashSet，HashMap， LinkedHashSet， LinkedHashMap， ArrayList
    *
    * */

    fun testList() { // list 有下面三种创建方式
        var numbers = listOf("one", "two", "three", "four") // 创建的是不可变的集合
        var numbersB = mutableListOf<Int>(1, 2, 3, 4, 5)
        var numberC = listOfNotNull(1, 2, 3)
        numbersB.add(10)
        var a = numberC.filter { it > 3 }

        var empty = emptyList<Int>() // 空集合

        println("---" + numbers.size + "---" + numbers[0] + "----" + numbers.get(1))
        println(numbersB.filter { it % 2 == 0 })
    }

    fun testSet() {  // set 有下面5中创建方式
        var numberA = setOf(1, 2, 3) // 创建的是不可变的集合
        var numberB = mutableSetOf(1, 2, 3)
        var numberC = hashSetOf(1, 2, 3)
        var numberD = linkedSetOf(1, 2, 3)
        var numberE = sortedSetOf(1, 2, 3)
        numberB.add(1)
        numberC.add(1)
        numberD.add(1)
        numberE.add(1)

        numberA.map { it * 2 }
        numberA.mapIndexed { idx, value -> value * idx }

        var empty = emptySet<Int>()
    }

    fun testMap() {
        var numberA = mapOf("a" to 1, "b" to 2, "c" to 3) // 不可变集合
        var numberB = mutableMapOf("a" to 1, "b" to 2, "c" to 3)
        var numberC = hashMapOf("a" to 1, "b" to 2, "c" to 3)
        var numberD = linkedMapOf("a" to 1, "b" to 2)

        numberB.put("f", 3)

        var empty = emptyMap<String, Int>()

    }

}