package cn.internetware.support.haixinyuan.syncclient.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by gongjianfei on 2017/4/19.
 */
@Entity
public class StatisticalInformation implements Comparable<StatisticalInformation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date statisticalTime;
    private int vesselTotal;
    private int vesselCreateTotal;
    private int vesselUpdateTotal;

    private int vesselPositionUpdateTotal;
    private int vesselPositionCreateTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    public int getVesselTotal() {
        return vesselTotal;
    }

    public void setVesselTotal(int vesselTotal) {
        this.vesselTotal = vesselTotal;
    }

    public int getVesselCreateTotal() {
        return vesselCreateTotal;
    }

    public void setVesselCreateTotal(int vesselCreateTotal) {
        this.vesselCreateTotal = vesselCreateTotal;
    }

    public int getVesselUpdateTotal() {
        return vesselUpdateTotal;
    }

    public void setVesselUpdateTotal(int vesselUpdateTotal) {
        this.vesselUpdateTotal = vesselUpdateTotal;
    }

    public int getVesselPositionUpdateTotal() {
        return vesselPositionUpdateTotal;
    }

    public void setVesselPositionUpdateTotal(int vesselPositionUpdateTotal) {
        this.vesselPositionUpdateTotal = vesselPositionUpdateTotal;
    }

    public int getVesselPositionCreateTotal() {
        return vesselPositionCreateTotal;
    }

    public void setVesselPositionCreateTotal(int vesselPositionCreateTotal) {
        this.vesselPositionCreateTotal = vesselPositionCreateTotal;
    }

    @Override
    public int compareTo(StatisticalInformation o) {
        return o.getStatisticalTime().compareTo(this.statisticalTime);
    }
}
