package cn.internetware.support.haixinyuan.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * VesselVoyage info.
 */
@Entity
public class VesselVoyage {
    @Id
    private Long shipid;
    private String mmsi;
    private String vesselType;

    private String status;
    private String draught;
    private String departure;
    private String destination;
    private Date atd; // Actual Time Of Departure;
    private Date eta; // Estimate Time Of Arrival;
    private Date ata; // Actual Time Of Arrival;
    private String maxSpeed;
    private String averageSpeed;
    private Date receivedTime;

    private Date crawlTime;
    private Date sendTime;

    public VesselVoyage() {
    }

    public VesselVoyage(Long shipid) {
        this.shipid = shipid;
    }

    public VesselVoyage(String mmsi) {
        this.mmsi = mmsi;
    }

    public VesselVoyage(Long shipid, String mmsi) {
        this.shipid = shipid;
        this.mmsi = mmsi;
    }

    @Override
    public String toString() {
        return String.format("VesselVoyage(shipid=%d, mmsi=%s, destination=%s)", shipid, mmsi, destination);
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

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDraught() {
        return draught;
    }

    public void setDraught(String draught) {
        this.draught = draught;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
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

    public String getVesselType() {
        return vesselType;
    }

    public void setVesselType(String vesselType) {
        this.vesselType = vesselType;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }
}

