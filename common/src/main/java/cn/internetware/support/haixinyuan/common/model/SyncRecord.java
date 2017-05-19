package cn.internetware.support.haixinyuan.common.model;

import cn.internetware.support.haixinyuan.common.util.TimeUtil;

import java.util.Date;

public class SyncRecord {
    public Long id;
    public Date profileTime;
    public Date photoTime;
    public Long eventPosition;

    public SyncRecord() {
    }

    public SyncRecord(Long id, Date profileTime, Date photoTime, Long eventPosition) {
        this.id = id;
        this.profileTime = profileTime;
        this.photoTime = photoTime;
        this.eventPosition = eventPosition;
    }

    public String toString() {
        return String.format("SyncRecord(id=%s, profileTime=%s, photoTime=%s, eventPosition=%s)", id,
                             TimeUtil.formatDate(profileTime),
                             TimeUtil.formatDate(photoTime),
                             eventPosition);
    }
}

