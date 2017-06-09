package cn.internetware.support.haixinyuan.syncclient.common;

import cn.internetware.support.haixinyuan.common.model.VesselPosition;
import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import cn.internetware.support.haixinyuan.common.model.VesselVoyage;
import cn.internetware.support.haixinyuan.syncclient.model.VesselStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class UdpRelayClientTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UdpRelayClientTest.class);
    @Test
    public void testSendPosition() throws IOException {
        UdpRelayClient client = new UdpRelayClient("127.0.0.1", 62017);

        VesselPosition position = new VesselPosition();
        position.setTime(new Date());
        position.setMmsi("123456789");
        position.setLongitude((double) -1.2f);
        position.setLatitude((double) -2.3f);
        position.setCourse(13.7f);
        position.setHeading(26.0f);
        position.setSpeed(6.8f);
        position.setStatus("Stopped");

        client.sendVesselPosition(position);
        LOGGER.info("Send position via udp: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(position));
        client.stop();
    }

    @Test
    public void testSendStatic() throws IOException {
        UdpRelayClient client = new UdpRelayClient("127.0.0.1", 62017);

        VesselStatus status = new VesselStatus();
        status.profile = new VesselProfile();
        status.voyage = new VesselVoyage();
        status.voyage.setReceivedTime(new Date());
        status.voyage.setMmsi("123456789");
        status.profile.setName("");
        status.profile.setCallSign("CallSign");
        status.profile.setLength("27.6m");
        status.profile.setBreadth(null);
        status.profile.setImo("987654321");
        status.voyage.setDestination("CN TJG");
        status.voyage.setEta(new Date());

        client.sendVesselStatic(status.profile, status.voyage);
        LOGGER.info("Send position via udp: {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(status));
        client.stop();
    }
}

