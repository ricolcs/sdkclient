package cn.internetware.support.haixinyuan.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class VesselPositionCurrent {
    @Id
    private Long shipid;
    private String mmsi;


    private Double latitude;
    private Double longitude;
    private Float speed;
    private Float course;
    private Float heading;
    private String area;
    private String status;
    private Date time;
    private Date createTime;

    private Date crawlTime;
    private Date sendTime;

    public VesselPositionCurrent() {
    }

    public VesselPositionCurrent(Long shipid) {
        this.shipid = shipid;
    }

    public VesselPositionCurrent(String mmsi) {
        this.mmsi = mmsi;
    }

    public VesselPositionCurrent(Long shipid, String mmsi) {
        this.shipid = shipid;
        this.mmsi = mmsi;
    }

   @Override
    public String toString() {
        return String.format("VesselPositionCurrent(shipid=%d, mmsi=%s, latitude=%f, longitude=%f)",
                shipid, mmsi, latitude, longitude);
    }


    public Long getShipid() {
        return shipid;
    }

    public void setShipid(Long shipid) {
        this.shipid = shipid;
    }

    public String getMmsi() {
        return mmsi;
    }

    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getCourse() {
        return course;
    }

    public void setCourse(Float course) {
        this.course = course;
    }

    public Float getHeading() {
        return heading;
    }

    public void setHeading(Float heading) {
        this.heading = heading;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
}

