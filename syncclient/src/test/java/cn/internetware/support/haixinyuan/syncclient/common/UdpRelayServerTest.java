package cn.internetware.support.haixinyuan.syncclient.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class UdpRelayServerTest {
    @Test
    public void testStart() throws Exception {
        UdpRelayServer server = new UdpRelayServer("127.0.0.1", 62017);
        Thread.sleep(3000000L);
        server.stop();
    }
}