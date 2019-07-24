package com.wz.kotlin_study.part01

/**
 * Created by wangzhan on 2019-07-01 13:58
 */

class method(val name: String, val firstName: String, val lastName: String, var age: Int) {
    val Cname = name  // 主构造函数参数可以在属性声明中使用

    init {
        print("name is $name")  // 主构造函数可以在init初始化中使用
    }

    // this 次构造方法 继承主构造方法，参数必须和主构造方法数量一致
    constructor(name: String) : this(name, "", "", 0) {

    }


}

class Person(val name: String, parent: Person) {
    // 次构造函数
    constructor(name: String, p: Person, s: Int) : this(name, p) /*主构造函数，两个参数*/{

    }
}

// 如果没有主或者次构造函数，会默认有个无惨的主构造函数，如果不想有public的构造函数，可以用private修饰
class privateClass private constructor() {
    fun test() {}
}

open class ClassA {
    open val age: Int get() = 123
    open fun test(): Int {
        return age
    }
}

open class ClassB : ClassA() {
    override val age: Int get() = 222
    final override fun test(): Int { // 不想再被覆盖，可以用final修饰
        System.out.println("------------" + (super.age + 1))
        return super.age + 1
    }

    inner class innerClassB {
        fun g() {
            var age = 0
            super@ClassB.test()
            println("+++++++" + super@ClassB.age)
        }
    }
}


interface inter {
    val count: Int
    fun justTest()
}

abstract class ClassC(override val count: Int) : ClassB(), inter {
    // 此时test方法被final修饰，所以无法被继承
    override abstract fun justTest()
}


