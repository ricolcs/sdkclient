package cn.internetware.support.haixinyuan.syncclient.common;

import cn.internetware.support.haixinyuan.syncclient.model.VesselStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UdpRelayServer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UdpRelayServer.class);
    private ExecutorService ioWorker = Executors.newSingleThreadExecutor();
    private ExecutorService bizWorker = Executors.newSingleThreadExecutor();
    protected static final ObjectMapper mapper = new ObjectMapper();

    private String ip;
    private int port;

    private final InetAddress address;
    private final DatagramSocket socket;

    private volatile boolean isStopped = false;

    UdpPacketParser parser = new UdpPacketParser();

    public static UdpRelayServer createByIpAndPort(String ipAndPort) {
        String[] ipAndPortArray = ipAndPort.split(":");
        return new UdpRelayServer(ipAndPortArray[0], Integer.parseInt(ipAndPortArray[1]));
    }

    public UdpRelayServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid server ip: " + ip, e);
        }
        try {
            socket = new DatagramSocket(port, address);
            socket.setSoTimeout(5000);
        } catch (SocketException e) {
            throw new RuntimeException("Create DatagramSocket failed.", e);
        }
        LOGGER.info("{}: server created.", this);

        start();
    }

    @Override
    public String toString() {
        return String.format("UdpRelayServer(ip=%s, port=%d)", ip, port);
    }

    protected UdpRelayServer start() {
        ioWorker.execute(() -> receivePacketLoop());
        LOGGER.info("{}: server started.", this);
        return this;
    }

    public UdpRelayServer receivePacketLoop() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        DatagramPacket packet = new DatagramPacket(byteBuffer.array(), byteBuffer.array().length);

        while (!isStopped) {
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                LOGGER.info("Udp socket receive failed: ", e);
                continue;
            }
            ByteBuffer bb = ByteBuffer.wrap(Arrays.copyOf(packet.getData(), packet.getLength()));
            bizWorker.execute(() -> {
                try {
                    onPacketReceived(bb);
                } catch (Exception e) {
                    LOGGER.error("{}: processing packet(length={}) failed.", this, bb.limit(), e);
                }
            });
        }

        return this;
    }

    protected void onPacketReceived(ByteBuffer bb) {
        LOGGER.debug("{}: received packet, length={}, data={}", this, bb.limit(), bb.array());
        VesselStatus status = parser.unpackVesselStatus(bb);
        if (bb.position() != bb.limit()) {
            throw new RuntimeException(String.format("Buffer data remaining un parsed, current consumed=%d, expected=%d", bb.position(), bb.limit()));
        }
        try {
            LOGGER.info("{}: received data={}", this, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(status));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Processing udp packet failed.", e);
        }
    }


    public UdpRelayServer stop() {
        isStopped = true;

        ioWorker.shutdownNow();
        while (!ioWorker.isTerminated()) {
            LOGGER.info("{}: waiting to shutdown...", this);
            try {
                ioWorker.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // Ignore it.
            }
        }
        LOGGER.info("{}: stopped.", this);
        return this;
    }
}

