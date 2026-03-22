package com.twisted.game.world.entity

fun AttributeKey.increment(p: Mob, value: Number = 1) {
    if (p.getAttrib<Any?>(this) !is Number) {
        System.err.println("cannot increment $this as it is not a Number")
        return
    }
    val v = p.getAttribOr(this, 0) as Number
    when (v) {
        is Long -> p.putAttrib(this, v.plus(value as Long))
        is Int -> p.putAttrib(this, v.plus(value as Int))
        is Byte -> p.putAttrib(this, v.plus(value as Byte))
        is Double -> p.putAttrib(this, v.plus(value as Double))
    }
}
fun AttributeKey.set(p: Mob, value: Any?) {
    p.putAttrib(this, value)
}
fun AttributeKey.int(p: Mob): Int = p.getAttribOr<Int>(this, 0)
fun AttributeKey.yes(p: Mob): Boolean = p.getAttribOr<Boolean>(this, false)
fun AttributeKey.nope(p: Mob): Boolean = !p.getAttribOr<Boolean>(this, false)
fun AttributeKey.strOrEmpty(p: Mob): String = p.getAttribOr<String>(this, "")
