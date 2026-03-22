package com.twisted.game.world.entity

import com.twisted.game.task.impl.TickableTask

fun Entity.event(task: TickableTask.() -> Unit) {
    repeatingTask {
        task.invoke(it)
    }
}
