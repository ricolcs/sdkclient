package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselPositionCurrent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface VesselPositionCurrentDao extends CrudRepository<VesselPositionCurrent, Long> {
    @Query("SELECT v FROM VesselPositionCurrent v WHERE v.time >= :updateTime")
    List<VesselPositionCurrent> findByUpdateTime(@Param("updateTime") Date updateTime);

    @Query("SELECT COUNT(*) FROM VesselPositionCurrent v WHERE v.createTime < :endTime AND v.createTime >= :startTime")
    int findVesselCreateCount(@Param("endTime")Date endTime, @Param("startTime")Date startTime);

    @Modifying
    @Query("DELETE FROM VesselPositionCurrent v WHERE v.createTime IS NOT NULL AND v.createTime <:date")
    int deletePositionOlderThan(@Param("date") Date date);
}

