package com.twisted.game.service

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object AutoBackup {

    private val log = LoggerFactory.getLogger(AutoBackup::class.java)

    private val source = File("data/saves")
    private val destination = File("data/backups")
    private var running = AtomicBoolean(false)

    @JvmStatic
    fun init() {
        val scheduler = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder()
            .setNameFormat("AutoBackup")
            .setDaemon(true)
            .build()
        )
        scheduler.scheduleAtFixedRate(::backupFiles, 0, 30, TimeUnit.MINUTES)
    }

    private fun backupFiles() {
        running.set(true)
        try {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date())
            val targetFile = destination.resolve("saves-$formattedDate.zip")

            check(targetFile.parentFile.exists() || targetFile.parentFile.mkdirs()) { "Failed to create backup directory" }
            ZipOutputStream(FileOutputStream(targetFile)).use {
                zipFile(it, source, source)
            }
        } catch (e: Throwable) {
            log.error("Failed to backup files", e)
        } finally {
            running.set(false)
        }
    }

    private fun zipFile(zos: ZipOutputStream, root: File, sourceFile: File) {
        if (sourceFile.isDirectory) {
            val files = sourceFile.listFiles() ?: return
            for (file in files) {
                zipFile(zos, root, file)
            }
        } else {
            val entry = ZipEntry(root.toURI().relativize(sourceFile.toURI()).path)
            zos.putNextEntry(entry)
            sourceFile.inputStream().use { input ->
                input.copyTo(zos)
            }
            zos.closeEntry()
        }
    }

    @JvmStatic
    fun waitUntilCompletion() {
        if (running.get()) {
            log.info("Waiting for backup to complete")
        }
        while (running.get()) {
            Thread.sleep(100)
        }
    }
}
