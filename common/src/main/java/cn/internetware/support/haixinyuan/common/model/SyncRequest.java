package cn.internetware.support.haixinyuan.common.model;

import cn.internetware.support.haixinyuan.common.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SyncRequest {
    // Request.
    public Long profileTimeStart;
    public Long photoTimeStart;
    public Long eventPositionStart;

    // Response vessel profile and position data.
    public Long code;
    public String message;
    public Long profileTimeEnd;
    public Long photoTimeEnd;
    public Long eventPositionEnd;

    public List<VesselProfile> vesselProfileList;
    public List<VesselPhoto> vesselPhotoList;
    public List<VesselVoyage> vesselVoyageList;
    public List<VesselPosition> vesselPositionList;

    public static SyncRequest fromString(String content) throws IOException {
        return new ObjectMapper().readValue(content, SyncRequest.class);
    }

    @Override
    public String toString() {
        return String.format(
                "SyncRequest(profileStart=%s, profileEnd=%s, photoStart=%s, photoEnd=%s, eventStart=%d, eventEnd=%d, " +
                "profileSize=%s, photoSize=%s, voyageSize=%s, positionSize=%s)",
                TimeUtil.formatTimestamp(profileTimeStart), TimeUtil.formatTimestamp(profileTimeEnd),
                TimeUtil.formatTimestamp(photoTimeStart), TimeUtil.formatTimestamp(photoTimeEnd),
                eventPositionStart, eventPositionEnd,
                vesselProfileList == null ? 0 : vesselProfileList.size(),
                vesselPhotoList == null ? 0 : vesselPhotoList.size(),
                vesselVoyageList == null ? 0 : vesselVoyageList.size(),
                vesselPositionList == null ? 0 : vesselPositionList.size());
    }
}

