package com.twisted.autobackup;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class PlateauBackup {

    private static final String BACKUP_PATH = System.getProperty("user.home") + "/Desktop/backups/";// here location of backups going be saved
    private static String CHARACTER_SAVES_PATH;
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> backup;
    private final long backup_period = TimeUnit.HOURS.toMillis(1);

    public void start() {
        backup = service.scheduleAtFixedRate(this::backup, backup_period, backup_period, TimeUnit.MILLISECONDS);
        CHARACTER_SAVES_PATH = "./data/saves/characters/";//here is location of characters going be taken every one hour we will change it now to 2 minute to see
        //or 1 minute

    }

    public void backup() {

        try {
            System.out.println("Performing character backup...");

            File folder = new File(BACKUP_PATH);

            if (!folder.exists())
                folder.mkdirs();

            File backup = new File(folder, "characters-" + DateFormatUtils.format(System.currentTimeMillis(), "MM-dd-yyyy HH-mm-ss") + ".zip");

            FileOutputStream fos = new FileOutputStream(backup);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File characters = new File(CHARACTER_SAVES_PATH);
            File[] saves = characters.listFiles();

            assert saves != null;

            for (File file : saves) {
                writeSaveToZip(file, zos);
            }

            zos.close();

            System.out.println("Successfully backed up {} character file(s)."+saves.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeSaveToZip(File file, ZipOutputStream zos) {
        try {

            //Create entry
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            //Write the data to the entry
            byte[] data = Files.readAllBytes(file.toPath());
            zos.write(data, 0, data.length);
            zos.closeEntry();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
