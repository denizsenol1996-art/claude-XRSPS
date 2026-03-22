package com.twisted.game.world.definition.dumper

import java.io.File
import java.nio.file.Files

fun main(args: Array<String>) {
    File("./data/combat/drops").listFiles()?.forEach {
        val lines = it.readLines().toMutableList()
        lines.forEachIndexed { index, s ->
            if (s.contains("\"id\": 995")) {
                lines[index] = "" // overrwrite line
                println("[${it.name} overwrite line $index")
            }
        }
        Files.write(it.toPath(), lines)
    }

    /*File("./data/combat/drops").listFiles()?.forEach {
        val lines = it.readLines().toMutableList()
        lines.forEachIndexed { index, s ->
            if (s.contains("\"PVPWorld\": true")) {
                lines[index] = "" // overrwrite line
                println("[${it.name} overwrite line $index")
            }
        }
        Files.write(it.toPath(), lines)
    }*/
}
