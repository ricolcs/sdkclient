package cn.internetware.support.haixinyuan.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SyncRequestTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SyncRequestTest.class);
    @Test
    public void testFromString() throws Exception {
        SyncRequest request = new SyncRequest();
        request.vesselPositionList = new ArrayList<VesselPosition>();
        request.vesselPositionList.add(new VesselPosition(3L));
        request.vesselPositionList.add(new VesselPosition(4L));
        String content = new ObjectMapper().writeValueAsString(request);
        LOGGER.info("Get content: {}", content);

        SyncRequest other = SyncRequest.fromString(content);
        LOGGER.info("Got other: {}", other.vesselPositionList);
    }
}