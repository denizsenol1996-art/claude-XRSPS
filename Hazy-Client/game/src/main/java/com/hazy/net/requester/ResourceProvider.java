package com.hazy.net.requester;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.zip.CRC32;

import com.hazy.Client;
import com.hazy.ClientConstants;
import com.hazy.cache.Archive;
import com.hazy.collection.LinkedList;
import com.hazy.collection.Queue;
import com.hazy.io.Buffer;

public final class ResourceProvider extends Provider {

    private int totalFiles;
    private final LinkedList requested;
    private int maximumPriority;
    public String loadingMessage;
    private int deadTime;
    private long lastRequestTime;
    private int[] landscapes;
    private final byte[] payload;
    public int tick;
    private final byte[][] fileStatus;
    private Client clientInstance;
    private final LinkedList extras;
    private int completedSize;
    private int remainingData;
    private int[] musicPriorities;
    public int errors;
    private int[] mapFiles;
    private int filesLoaded;
    private boolean running;
    private OutputStream outputStream;
    private int[] membersArea;
    private boolean expectingData;
    private final LinkedList complete;
    private final byte[] gzipInputBuffer;
    private int[] anIntArray1360;
    private final Queue requests;
    private InputStream inputStream;
    private Socket socket;
    private final int[][] versions;
    private int uncompletedCount;
    private int completedCount;
    private final LinkedList unrequested;
    private Resource current;
    private final LinkedList mandatoryRequests;
    private int[] areas;
    private byte[] modelIndices;
    private int idleTime;
    private final CRC32 crc32;

    public String currentDownload = "";

    private String forId(int type) {
        switch (type) {
            case 1:
                return "Model";
            case 2:
                return "Animation";
            case 3:
                return "Sound";
            case 4:
                return "Map";
        }
        return "";
    }

    public ResourceProvider() {
        requested = new LinkedList();
        loadingMessage = "";
        payload = new byte[500];
        fileStatus = new byte[4][];
        extras = new LinkedList();
        running = true;
        expectingData = false;
        complete = new LinkedList();
        gzipInputBuffer = new byte[0x71868];
        requests = new Queue();
        versions = new int[4][];
        unrequested = new LinkedList();
        mandatoryRequests = new LinkedList();
        crc32 = new CRC32();
    }



    public int[] file_amounts = new int[4];

    private final String crcNames[] = {"model_crc", "anim_crc", "midi_crc", "map_crc"};
    private final int[][] crcs = new int[crcNames.length][];
    private boolean debugCheapHaxValues = false;

    public void initialize(Archive archive, Client client) {

        for (int i = 0; i < crcNames.length; i++) {
            byte[] crc_file = archive.get(crcNames[i]);
            int length = 0;

            if (crc_file != null) {
                length = crc_file.length / 4;
                Buffer crcStream = new Buffer(crc_file);
                crcs[i] = new int[length];
                fileStatus[i] = new byte[length];
                for (int ptr = 0; ptr < length; ptr++) {
                    crcs[i][ptr] = crcStream.readInt();
                }
            }
        }


        //  byte[] data = FileUtils.read(ClientConstants.DATA_DIR + "/map_index"); //archive.get("map_index");


        byte[] data = archive.get("map_index");
        Buffer stream = new Buffer(data);
        int j1 = stream.readUnsignedShort();//data.length / 6;
        areas = new int[j1];
        mapFiles = new int[j1];
        landscapes = new int[j1];
        file_amounts[3] = j1;
        for (int i2 = 0; i2 < j1; i2++) {
            areas[i2] = stream.readUnsignedShort();
            mapFiles[i2] = stream.readUnsignedShort();
            landscapes[i2] = stream.readUnsignedShort();
            final int finalI2 = i2;
            //To find region ID (to get coords) from cheapHaxValues array:
            if (debugCheapHaxValues && Arrays.stream(cheapHaxValues).anyMatch(id -> id == mapFiles[finalI2])) {
                System.out.println("Area: " + areas[i2]);
            }
            //The cheapHaxValues regions seem to be tutorial island, and a couple quest areas, and sorceress's garden
        }

        System.out.println(String.format("Loaded %d maps loading OSRS version %d and SUB version %d", file_amounts[3], ClientConstants.OSRS_DATA_VERSION, ClientConstants.OSRS_DATA_SUB_VERSION));

        data = archive.get("midi_index");
        stream = new Buffer(data);
        j1 = data.length;
        file_amounts[2] = j1;
        musicPriorities = new int[j1];
        for (int k2 = 0; k2 < j1; k2++)
            musicPriorities[k2] = stream.readUnsignedByte();
        System.out.println(String.format("Loaded %d sounds loading OSRS version %d and SUB version %d", file_amounts[2], ClientConstants.OSRS_DATA_VERSION, ClientConstants.OSRS_DATA_SUB_VERSION));

        //For some reason, model_index = anim_index and vice versa
        data = archive.get("model_index");
        file_amounts[1] = data.length;

        data = archive.get("anim_index");
        file_amounts[0] = data.length;
        System.out.println(String.format("Loaded %d models loading OSRS version %d and SUB version %d", file_amounts[0], ClientConstants.OSRS_DATA_VERSION, ClientConstants.OSRS_DATA_SUB_VERSION));

        clientInstance = client;
        running = true;

    }

    public boolean passive_request(int fileId, int indexId) {
        return loadData(indexId, fileId, true);
    }

    public int getVersionCount(int index) {
        return versions[index].length;
    }

    public int getAnimCount() {
        return 33568;
    }

    public int getModelCount() {
        return 120000;
    }

    @Override
    public void provide(int file) {
        provide(0, file);
    }

    public boolean provide(int type, int file) {
        return loadData(type, file, true);
    }

    public boolean loadData(int indexID, int fileID, boolean flush) {
        Client.instance.getSwiftFUP().getFileRequests().file(indexID + 1, fileID);
        return true;
    }

    public int getModelIndex(int i) {
        return modelIndices[i] & 0xff;
    }

    public int getMapIdForRegions(int landscapeOrObject, int regionY, int regionX) {
        int code = (regionX << 8) + regionY;
        for (int area = 0; area < areas.length; area++) {
            if (areas[area] == code) {
                if (landscapeOrObject == 0) {
                    return mapFiles[area] > 9999 ? -1 : mapFiles[area];
                } else {
                    return landscapes[area] > 9999 ? -1 : landscapes[area];
                }
            }
        }
        //End of originally commented out code uncommented for OSRS data 190

        int regionId = (regionX << 8) + regionY;
        for (int j1 = 0; j1 < areas.length; j1++)
            if (areas[j1] == regionId) {
                if (regionId == 11840) {
                    System.out.println("m="+mapFiles[j1]);
                    System.out.println("l="+landscapes[j1]);
                }
                if (landscapeOrObject == 0) {
                    //Soulwars
                    if (mapFiles[j1] >= 3700 && mapFiles[j1] <= 3840)
                        return mapFiles[j1];
                    for (int cheapHax : mapFiles)
                        if (mapFiles[j1] == cheapHax)
                            return mapFiles[j1];
                    return mapFiles[j1] > 3535 ? -1 : mapFiles[j1];
                } else {
                    if (landscapes[j1] >= 3700 && landscapes[j1] <= 3840)
                        return landscapes[j1];
                    for (int cheapHax : cheapHaxValues)
                        if (landscapes[j1] == cheapHax)
                            return landscapes[j1];
                    return landscapes[j1] > 3535 ? -1 : landscapes[j1];
                }
            }
        return -1;

        /*int regionId = (regionX << 8) + regionY;
        for (int j1 = 0; j1 < areas.length; j1++)
            if (areas[j1] == regionId) {
                if (landscapeOrObject == 0) {
                    return mapFiles[j1];
                } else {
                    return landscapes[j1];
                }
            }*/
    }

    int[] cheapHaxValues = new int[]{
        3627, 3628,
        3655, 3656,
        3625, 3626,
        3629, 3630,
        4071, 4072,
        5253, 1816,
        1817, 3653,
        3654, 4067,
        4068, 3639,
        3640, 1976,
        1977, 3571,
        3572, 5129,
        5130, 2066,
        2067, 3545,
        3546, 3559,
        3560, 3569,
        3570, 3551,
        3552, 3579,
        3580, 3575,
        3576, 1766,
        1767, 3547,
        3548, 3682,
        3683, 3696,
        3697, 3692,
        3693, 4013,
        4079, 4080,
        4082, 3996,
        4083, 4084,
        4075, 4076,
        3664, 3993,
        3994, 3995,
        4077, 4078,
        4073, 4074,
        4011, 4012,
        3998, 3999,
        4081,


        6400, 6401
    };

    public boolean requestExtra(byte priority, int type, int file) {
        return loadData(type, file, true);
    }



}
