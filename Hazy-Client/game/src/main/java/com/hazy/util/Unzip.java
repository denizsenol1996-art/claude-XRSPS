package com.hazy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param deleteAfter        Should the zip file be deleted afterwards?
     */
    public static void unZipIt(String zipFile, String outputFolder, boolean deleteAfter) {

        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            boolean isDirectoryCreated = folder.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = folder.mkdir();
            }
            if (isDirectoryCreated) {

                //get the zip file content
                ZipInputStream zis =
                    new ZipInputStream(Files.newInputStream(Paths.get(zipFile)));
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {

                    String fileName = ze.getName();
                    File newFile = new File(outputFolder + File.separator + fileName);

                    System.out.println("file unzip : " + newFile.getAbsoluteFile());

                    //create all non exists folders
                    //else you will hit FileNotFoundException for compressed folder
                    new File(newFile.getParent()).mkdirs();
                    //Make sure it's not a directory, otherwise this will throw an Exception trying to write it.
                    if (!ze.isDirectory()) {
                        FileOutputStream fos = new FileOutputStream(newFile);

                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }

                        fos.close();
                    }
                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();

                if (deleteAfter) {
                    new File(zipFile).delete();
                }
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
