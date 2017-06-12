package cn.internetware.support.haixinyuan.syncclient.common;

import cn.internetware.support.haixinyuan.common.model.VesselPosition;
import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import cn.internetware.support.haixinyuan.common.model.VesselVoyage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UdpRelayClient {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UdpRelayClient.class);

    private ExecutorService sendDataWorker = Executors.newSingleThreadExecutor();

    private String ip;
    private int port;

    private final InetAddress address;
    private final DatagramSocket socket;

    UdpPacketParser parser = new UdpPacketParser();

    public static UdpRelayClient createByIpAndPort(String ipAndPort) {
        String[] ipAndPortArray = ipAndPort.split(":");
        return new UdpRelayClient(ipAndPortArray[0], Integer.parseInt(ipAndPortArray[1]));
    }

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
        LOGGER.info("{}: udp client created.", this);
    }

    @Override
    public String toString() {
        return String.format("UdpRelayClient(ip=%s, port=%s)", ip, port);
    }

    public void sendVesselPositionAsync(VesselPosition position) {
        sendDataWorker.execute(() -> {
            try {
                sendVesselPosition(position);
            } catch (Exception e) {
                LOGGER.error("{}: Send vessel position failed, position={}", position, e);
            }
        });
    }

    public void sendVesselStaticAsync(VesselProfile profile, VesselVoyage voyage) {
        sendDataWorker.execute(() -> {
            try {
                sendVesselStatic(profile, voyage);
            } catch (Exception e) {
                LOGGER.error("{}: Send vessel static failed, profile={}, voyage={}", profile, voyage, e);
            }
        });
    }

    public void sendVesselPosition(VesselPosition position) throws IOException {
        sendData(parser.packVesselPosition(position));
    }

    public void sendVesselStatic(VesselProfile profile, VesselVoyage voyage) throws IOException {
        sendData(parser.packVesselStatic(profile, voyage));
    }

    public UdpRelayClient stop() {
        sendDataWorker.shutdown();
        while (!sendDataWorker.isTerminated()) {
            LOGGER.info("{}: waiting to shutdown...", this);
            try {
                sendDataWorker.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // Ignore it.
            }
        }
        return this;
    }

    protected void sendData(ByteBuffer bb) throws IOException {
        DatagramPacket packet = new DatagramPacket(bb.array(), bb.arrayOffset(), bb.position(), address, port);
        socket.send(packet);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{}: send data packet, length={}, data={}", this, packet.getLength(), Arrays.copyOf(packet.getData(), packet.getLength()));
        }
    }
}

