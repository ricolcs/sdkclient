package cn.internetware.support.haixinyuan.syncclient.common;

import cn.internetware.support.haixinyuan.common.model.VesselPosition;
import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import cn.internetware.support.haixinyuan.common.model.VesselVoyage;
import cn.internetware.support.haixinyuan.syncclient.model.VesselStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpRelayClient {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UdpRelayClient.class);
    protected static final int MAX_NAME_LENGTH = 30;
    protected static final Map<String, Integer> statusToCodeMap = constructStatusMap();
    protected static Map<Integer, String> codeToStatusMap = constructCodeMap(statusToCodeMap);

    private ExecutorService sendDataWorker = Executors.newSingleThreadExecutor();

    protected static Map<String, Integer> constructStatusMap() {
        Map<String, Integer> result = new TreeMap<>();
        result.put("Aground", 0);
        result.put("Aid To Navigation", 1);
        result.put("At Anchor", 2);
        result.put("Base Station", 3);
        result.put("Class B", 4);
        result.put("Constrained By Her Draught", 5);
        result.put("Engaged In Fishing", 6);
        result.put("Moored", 7);
        result.put("Nav Aid - Sart - Mob", 8);
        result.put("Not Under Command", 9);
        result.put("Pushing Ahead - Towing", 10);
        result.put("Restricted Manoeuvrability", 11);
        result.put("SAR Aircraft", 12);
        result.put("Stopped", 13);
        result.put("Towing Astern", 14);
        result.put("Underway", 15);
        result.put("Underway By Sail", 16);
        result.put("Underway Using Engine", 17);
        return result;
    }

    protected static Map<Integer, String> constructCodeMap(Map<String, Integer> statusToCodeMap) {
        Map<Integer, String> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : statusToCodeMap.entrySet()) {
            result.put(entry.getValue(), entry.getKey());
        }
        return result;
    }


    private String ip;
    private int port;

    private final InetAddress address;
    private final DatagramSocket socket;

    private final ByteBuffer writeBuffer = ByteBuffer.allocate(4096);
    private final ByteBuffer readBuffer = ByteBuffer.allocate(4096);

    public UdpRelayClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid target ip: " + ip, e);
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Create DatagramSocket failed.", e);
        }
    }

    @Override
    public String toString() {
        return String.format("UdpRelayClient(ip=%s, port=%s)", ip, port);
    }

    public void sendVesselPosition(VesselPosition position) throws IOException {
        sendDataAsync(packVesselPosition(position));
    }

    public void sendVesselStatic(VesselProfile profile, VesselVoyage voyage) throws IOException {
        sendDataAsync(packVesselStatic(profile, voyage));
    }

    protected ByteBuffer packVesselPosition(VesselPosition position) {
        ByteBuffer bb = ByteBuffer.allocate(128);

        bb.putShort((short) 0x0121); // MsgID
        int positionOfPacketLengthField = bb.putShort((short) 0).position(); // length of the remaining content.
        bb.putInt(packDate(position.getTime()));
        bb.putInt(1); // Ais message id.
        bb.putInt(packMmsi(position.getMmsi()));
        bb.putInt((int) (1000000 * position.getLongitude()));
        bb.putInt((int) (1000000 * position.getLatitude()));
        bb.putFloat(packFloat(position.getCourse()));
        bb.putFloat(packFloat(position.getHeading()));
        bb.putFloat(packFloat(position.getSpeed()));
        bb.putChar(packStatus(position.getStatus())); // status.
        bb.putShort((short) 0); // RoT.
        int positionOfEnd = bb.position();

        // Finally, set the packet size.
        bb.position(positionOfPacketLengthField);
        bb.putInt(positionOfEnd - positionOfPacketLengthField);
        bb.position(positionOfEnd);

        return bb;
    }

    protected VesselPosition parseVesselPosition(ByteBuffer bb) {
        VesselPosition position = new VesselPosition();
        bb.getShort(); // MsgId.
        bb.getShort(); // packet length.
        bb.getInt(); // Ais message id.
        position.setMmsi(unpackToMmsi(bb.getInt()));
        position.setLongitude(((double) bb.getInt()) / 1000000);
        position.setLatitude(((double) bb.getInt()) / 1000000);
        position.setCourse(unpackToFloat(bb.getFloat()));
        position.setHeading(unpackToFloat(bb.getFloat()));
        position.setSpeed(unpackToFloat(bb.getFloat()));
        position.setStatus(unpackToStatus(bb.getChar()));
        bb.getShort(); // RoT.
        return position;
    }

    protected ByteBuffer packVesselStatic(VesselProfile profile, VesselVoyage voyage) {
        ByteBuffer bb = ByteBuffer.allocate(128);

        bb.putShort((short) 0x0120); // msgid.
        int positionOfPacketLengthField = bb.putShort((short) 0).position(); // length of the remaining content.
        bb.putInt(packDate(voyage.getReceivedTime() != null ? voyage.getReceivedTime() : voyage.getCrawlTime()));
        bb.putInt(5); // AisMsgId
        bb.putInt(packMmsi(voyage.getMmsi()));
        writeString(bb, profile.getName());
        writeString(bb, profile.getCallSign());
        bb.putFloat(packLength(profile.getLength()));
        bb.putFloat(packLength(profile.getBreadth()));
        bb.putInt(packMmsi(profile.getImo()));
        bb.putFloat(Float.NaN); // ship draft.
        bb.putChar((char)1); // ship type.
        bb.putInt(0); // cargo type.
        writeString(bb, voyage.getDestination());
        bb.putInt(packDate(voyage.getEta()));
        bb.putInt(0); // Location device type.
        int positionOfEnd = bb.position();

        // Finally, set the packet size.
        bb.position(positionOfPacketLengthField);
        bb.putInt(positionOfEnd - positionOfPacketLengthField);
        bb.position(positionOfEnd);

        return bb;
    }

    protected VesselStatus unpackvesselStatic(ByteBuffer bb) {
        VesselStatus status = new VesselStatus();
        status.profile = new VesselProfile();
        status.voyage = new VesselVoyage();

        bb.getShort(); // msgid.
        bb.getShort(); // packet size.
        bb.getInt(); // Ais msgid.
        status.profile.setMmsi(unpackToMmsi(bb.getInt()));
        status.voyage.setMmsi(status.profile.getMmsi());
        status.profile.setName(readString(bb));
        status.profile.setCallSign(readString(bb));
        status.profile.setLength(unpackToLength(bb.getFloat()));
        status.profile.setBreadth(unpackToLength(bb.getFloat()));
        status.profile.setImo(unpackToMmsi(bb.getInt()));
        bb.getFloat(); // ship draft.
        bb.getInt(); // cargo type.
        status.voyage.setDestination(readString(bb));
        status.voyage.setEta(unpackToDate(bb.getInt()));
        bb.getInt();

        return status;
    }

    protected void sendDataAsync(ByteBuffer bb) throws IOException {
        sendDataWorker.execute(() -> sendDataSyncNoException(bb));
    }

    protected void sendDataSyncNoException(ByteBuffer bb) {
        try {
            sendDataAsync(bb);
        } catch (IOException e) {
            LOGGER.error("{}: send data of of length {} failed.", this, bb.position(), e);
        }
    }
    protected void sendDataSync(ByteBuffer bb) throws IOException {
        DatagramPacket packet = new DatagramPacket(bb.array(), bb.arrayOffset(), bb.position());
        socket.send(packet);
    }

    private char packStatus(String status) {
        int code = 15;
        if (status != null && statusToCodeMap.containsKey(status)) {
            code = statusToCodeMap.get(status);
        }
        return (char) code;
    }

    private String unpackToStatus(char code) {
        return codeToStatusMap.get((int) code);
    }

    private int packMmsi(String mmsi) {
        if (mmsi != null) {
            return Integer.parseInt(mmsi);
        }
        return 0;
    }

    private String unpackToMmsi(int code) {
        if (code == 0) {
            return null;
        }
        return String.valueOf(code);
    }

    private float packFloat(Float input) {
        if (input == null) {
            return Float.NaN;
        }
        return input;
    }

    private Float unpackToFloat(float input) {
        if (Float.isNaN(input)) {
            return null;
        }
        return input;
    }

    private void writeString(ByteBuffer bb, String data) {
        if (data == null) {
            data = "";
        }
        bb.putChar((char) data.getBytes().length);
        bb.put(data.getBytes());
    }

    private String readString(ByteBuffer bb) {
        byte[] array = new byte[bb.getChar()];
        bb.get(array);
        return new String(array);
    }

    private int packDate(Date date) {
        int time = 0;
        if (date != null) {
            time = (int) (date.getTime() / 1000);
        }
        return time;
    }

    private Date unpackToDate(int time) {
        if (time <= 0) {
            return null;
        }
        return new Date(time * 1000);
    }

    private float packLength(String input) {
        float shipLength = Float.NaN;
        if (input  != null) {
            shipLength = Float.parseFloat(input.substring(0, input.length() - 1));
        }
        return shipLength;
    }

    private String unpackToLength(float input) {
        if (Float.isNaN(input)) {
            return null;
        }
        return String.format("%.1fm", input);
    }
}

