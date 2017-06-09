package cn.internetware.support.haixinyuan.syncclient.common;

import cn.internetware.support.haixinyuan.common.model.VesselPosition;
import cn.internetware.support.haixinyuan.common.model.VesselPositionCurrent;
import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import cn.internetware.support.haixinyuan.common.model.VesselVoyage;
import cn.internetware.support.haixinyuan.syncclient.model.VesselStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class UdpPacketParser {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UdpPacketParser.class);
    protected static final int MAX_NAME_LENGTH = 60;
    protected static final Map<String, Integer> statusToCodeMap = constructStatusMap();
    protected static Map<Integer, String> codeToStatusMap = constructCodeMap(statusToCodeMap);

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

    public UdpPacketParser() {
    }

    protected ByteBuffer packVesselPosition(VesselPosition position) {
        ByteBuffer bb = ByteBuffer.allocate(128);

        int positionOfPacketLengthField = bb.putShort((short) 0x0121).position(); // MsgID
        bb.putShort((short) 0); // length of the remaining content.
        bb.putInt(packDate(position.getTime()));
        bb.putInt(1); // Ais message id.
        bb.putInt(packFromStringToInt(position.getMmsi()));
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
        bb.putShort((short) (positionOfEnd - positionOfPacketLengthField));
        bb.position(positionOfEnd);

        return bb;
    }

    protected VesselStatus unpackVesselPosition(ByteBuffer bb) {
        VesselStatus status = new VesselStatus();
        status.position = new VesselPositionCurrent();
        bb.getShort(); // MsgId.
        bb.getShort(); // packet length.
        status.position.setTime(unpackToDate(bb.getInt()));
        bb.getInt(); // Ais message id.
        status.position.setMmsi(unpackFromIntToString(bb.getInt()));
        status.position.setLongitude(((double) bb.getInt()) / 1000000);
        status.position.setLatitude(((double) bb.getInt()) / 1000000);
        status.position.setCourse(unpackToFloat(bb.getFloat()));
        status.position.setHeading(unpackToFloat(bb.getFloat()));
        status.position.setSpeed(unpackToFloat(bb.getFloat()));
        status.position.setStatus(unpackToStatus(bb.getChar()));
        bb.getShort(); // RoT.
        return status;
    }

    protected ByteBuffer packVesselStatic(VesselProfile profile, VesselVoyage voyage) {
        ByteBuffer bb = ByteBuffer.allocate(128);

        int positionOfPacketLengthField = bb.putShort((short) 0x0120).position(); // msgid.
        bb.putShort((short) 0); // length of the remaining content.
        bb.putInt(packDate(voyage.getReceivedTime() != null ? voyage.getReceivedTime() : voyage.getCrawlTime()));
        bb.putInt(5); // AisMsgId
        bb.putInt(packFromStringToInt(voyage.getMmsi()));
        writeString(bb, profile.getName());
        writeString(bb, profile.getCallSign());
        bb.putFloat(packLength(profile.getLength()));
        bb.putFloat(packLength(profile.getBreadth()));
        bb.putInt(packFromStringToInt(profile.getImo()));
        bb.putFloat(Float.NaN); // ship draft.
        bb.putChar((char) 1); // ship type.
        bb.putInt(0); // cargo type.
        writeString(bb, voyage.getDestination());
        bb.putInt(packDate(voyage.getEta()));
        bb.putInt(0); // Location device type.
        int positionOfEnd = bb.position();

        // Finally, set the packet size.
        bb.position(positionOfPacketLengthField);
        bb.putShort((short) (positionOfEnd - positionOfPacketLengthField));
        bb.position(positionOfEnd);

        return bb;
    }

    protected VesselStatus unpackvesselStatic(ByteBuffer bb) {
        VesselStatus status = new VesselStatus();
        status.profile = new VesselProfile();
        status.voyage = new VesselVoyage();

        bb.getShort(); // msgid.
        bb.getShort(); // packet size.
        status.voyage.setReceivedTime(unpackToDate(bb.getInt()));
        bb.getInt(); // Ais msgid.
        status.profile.setMmsi(unpackFromIntToString(bb.getInt()));
        status.voyage.setMmsi(status.profile.getMmsi());
        status.profile.setName(readString(bb));
        status.profile.setCallSign(readString(bb));
        status.profile.setLength(unpackToLength(bb.getFloat()));
        status.profile.setBreadth(unpackToLength(bb.getFloat()));
        status.profile.setImo(unpackFromIntToString(bb.getInt()));
        bb.getFloat(); // ship draft.
        bb.getChar();
        bb.getInt(); // cargo type.
        status.voyage.setDestination(readString(bb));
        status.voyage.setEta(unpackToDate(bb.getInt()));
        bb.getInt();

        return status;
    }

    protected VesselStatus unpackVesselStatus(ByteBuffer bb) {
        int position = bb.position();
        short msgid = bb.getShort();
        bb.position(position);
        VesselStatus status = null;
        if (msgid == 0x0120) {
            status = unpackvesselStatic(bb);
        } else if (msgid == 0x0121) {
            status = unpackVesselPosition(bb);
        } else {
            throw new RuntimeException(String.format("Unknown message id=%d", msgid));
        }
        return status;
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

    private int packFromStringToInt(String input) {
        if (input != null) {
            return Integer.parseInt(input);
        }
        return 0;
    }

    private String unpackFromIntToString(int code) {
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
        if (data.length() > MAX_NAME_LENGTH) {
            data = data.substring(0, MAX_NAME_LENGTH);
        }
        bb.putChar((char) data.getBytes().length);
        bb.put(data.getBytes());
    }

    private String readString(ByteBuffer bb) {
        int length = bb.getChar();
        if (length > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "String length exceed max=%d, actual=%d", MAX_NAME_LENGTH, length));
        }
        byte[] array = new byte[length];
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
        return new Date((long) time * 1000);
    }

    private float packLength(String input) {
        float shipLength = Float.NaN;
        if (input != null) {
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

