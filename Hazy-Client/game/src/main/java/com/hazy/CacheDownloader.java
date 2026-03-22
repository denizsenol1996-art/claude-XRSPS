package com.hazy;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.hazy.sign.SignLink;
import com.hazy.util.Unzip;
import com.google.common.base.Preconditions;
import net.runelite.client.RuneLite;

public class CacheDownloader implements Runnable {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(CacheDownloader.class.getName());

    private static final String CACHE_DOWNLOAD_LINK = "https://hazyrealm.com/client/osrs.zip";


    public static int cacheVersionRemote;
    public static int cacheVersionLocal;

    private final Client client;

    private static final int BUFFER = 1024;

    private final Path fileLocation;

    public CacheDownloader(Client client) {
        RuneLite.CACHE_DIR_OSRS.mkdir();
        Objects.requireNonNull(RuneLite.CACHE_DIR_OSRS.getAbsolutePath());
        this.client = client;
        fileLocation = Paths.get(RuneLite.CACHE_DIR_OSRS.getAbsolutePath(), getArchivedName());
    }

    private int getLocalVersion() {
        try(BufferedReader fr = new BufferedReader(new FileReader(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + File.separator + "version.dat"))){
            return Integer.parseInt(fr.readLine());
        } catch (Exception e) {
            return -1;
        }
    }

    public void writeVersion(int version) {
        File versionFile = new File(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + File.separator + "version.dat");
        if(versionFile.exists())
            versionFile.delete();
        try(BufferedWriter br = new BufferedWriter(new FileWriter(versionFile))) {
            br.write(version + "");
            br.flush();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteZip() {
        try {
            Files.deleteIfExists(fileLocation);
        } catch (IOException ex) {
            log.severe("Cannot delete cache zip file.");
            ex.printStackTrace();
        }
    }

    public CacheDownloader downloadCache() {
        try {
            File location = new File(RuneLite.CACHE_DIR_OSRS.getAbsolutePath());
            File version = new File(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + "/version.dat");
            cacheVersionRemote  = 31;
            if (!location.exists() || !version.exists()) {
                log.info("Cache does not exist, downloading.");
                update();
            } else {
                cacheVersionLocal = getLocalVersion();
                log.info("Cache version local=" + cacheVersionLocal + ", remote=" + cacheVersionRemote);
                if (cacheVersionRemote != cacheVersionLocal) {
                    update();
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void update() {
        downloadFile(getArchivedName());
        Unzip.unZipIt(fileLocation.toString(), RuneLite.CACHE_DIR_OSRS.getAbsolutePath(), true);
        writeVersion(cacheVersionRemote);
        deleteZip();
    }

    private void downloadFile(String localFileName) {
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + "/" + localFileName)))) {
            URL url = new URL(CacheDownloader.CACHE_DOWNLOAD_LINK);
            URLConnection conn = url.openConnection();
            try (InputStream in = conn.getInputStream()) {
                byte[] data = new byte[BUFFER];
                int numRead;
                long numWritten = 0;
                int fileSize = conn.getContentLength();
                long startTime = System.currentTimeMillis();

                while ((numRead = in.read(data)) != -1) {
                    out.write(data, 0, numRead);
                    numWritten += numRead;

                    int percentage = (int) (((double) numWritten / (double) fileSize) * 100D);
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    int downloadSpeed = (int) ((numWritten / 1024) / (1 + (elapsedTime / 1000)));

                    float speedInBytes = 1000f * numWritten / elapsedTime;
                    int timeRemaining =  (int) ((fileSize - numWritten) / speedInBytes);

                    client.draw_loadup(percentage, ClientConstants.CLIENT_NAME + " - Downloading OSRS Cache ");
                }
            }
            out.flush();
        } catch (IOException ex) {
            // handle the exception here, if necessary
            ex.printStackTrace();
        }
    }

    private String getArchivedName() {
        int lastSlashIndex = CACHE_DOWNLOAD_LINK.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            String u = CACHE_DOWNLOAD_LINK.substring(lastSlashIndex + 1);
            return u.replace("?dl=1", "");
        } else {
            System.err.println("error retrieving archived name.");
        }
        return "";
    }

    private void unZip() throws IOException {
        InputStream in = new BufferedInputStream(Files.newInputStream(Paths.get(fileLocation.toString())));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry e;
        int files = countRegularFiles(new ZipFile(fileLocation.toString()));

        int numWritten = 0;
        while ((e = zin.getNextEntry()) != null) {
            String fileName = e.getName();
            File newFile = new File(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + File.separator + fileName);
            if (e.isDirectory()) {
                (new File(RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + e.getName())).mkdir();
            } else {
                int percentage = (int) (((double) numWritten++ / (double) files) * 100D);
                client.draw_loadup(percentage, ClientConstants.CLIENT_NAME + " - Installing Cache " + percentage + "%");
                if (e.getName().equals(fileLocation.toString())) {
                    unzip(zin, RuneLite.CACHE_DIR_OSRS.getAbsolutePath());
                    break;
                }
                File file = new File(newFile.getParent());
                if (!file.exists()) {
                    Preconditions.checkState(file.mkdirs(), "Cannot create file.");
                }
                unzip(zin, RuneLite.CACHE_DIR_OSRS.getAbsolutePath() + e.getName());
            }
        }
        in.close();
        zin.close();
    }

    private static int countRegularFiles(final ZipFile zipFile) {
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        int numRegularFiles = 0;
        while (entries.hasMoreElements()) {
            if (! entries.nextElement().isDirectory()) {
                ++numRegularFiles;
            }
        }
        log.info("Number of regular files in zip: " + numRegularFiles);
        return numRegularFiles;
    }

    private void unzip(ZipInputStream zin, String s) throws IOException {
        try (FileOutputStream out = new FileOutputStream(s)) {
            byte[] b = new byte[BUFFER];
            int len;
            while ((len = zin.read(b)) != -1)
                out.write(b, 0, len);
            out.flush();
        }
    }

    @Override
    public void run() {
        downloadCache();
    }
}
