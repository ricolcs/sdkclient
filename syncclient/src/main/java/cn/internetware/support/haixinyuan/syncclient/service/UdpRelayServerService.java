package cn.internetware.support.haixinyuan.syncclient.service;

import cn.internetware.support.haixinyuan.syncclient.common.UdpRelayServer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Profile({"server"})
@Service
public class UdpRelayServerService {
    private final UdpRelayServer udpRelayServer;
    public UdpRelayServerService() {
        udpRelayServer = UdpRelayServer.createByIpAndPort(System.getProperty("haixinyuan.relay.target", "192.168.10.24:8888"));
    }

    @PreDestroy
    public void dispose() {
        udpRelayServer.stop();
    }
}

