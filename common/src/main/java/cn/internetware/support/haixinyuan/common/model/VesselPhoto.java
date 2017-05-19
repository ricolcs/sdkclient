package cn.internetware.support.haixinyuan.common.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class VesselPhoto {
    @Id
    private Long id;
    private Long shipid;
    private Date crawlTime;
    private Date sendTime;

    @Transient
    private byte[] content;

    public VesselPhoto() {
    }

    public VesselPhoto(Long id, Long shipid) {
        this.id = id;
        this.shipid = shipid;
    }

    @Override
    public String toString() {
        return String.format("VesselPhoto(id=%s, shipid=%s, contentSize=%s)", id, shipid, content == null ? null : content.length);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipid() {
        return shipid;
    }

    public void setShipid(Long shipid) {
        this.shipid = shipid;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}

