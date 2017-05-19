package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface VesselProfileDao extends CrudRepository<VesselProfile, Long> {
    @Query("SELECT v FROM VesselProfile v WHERE v.updateTime IS NULL OR v.updateTime < :updateTime ORDER BY updateTime ASC")
    List<VesselProfile> findVesselToUpdate(@Param("updateTime") Date updateTime, Pageable pageable);

    @Query("SELECT COUNT(*) FROM VesselProfile")
    int findAllCount();

    @Query("SELECT COUNT(*) FROM VesselProfile v WHERE v.createTime < :endTime AND v.createTime >= :startTime")
    int findCreateCount(@Param("endTime")Date endTime, @Param("startTime")Date startTime);

    @Query("SELECT COUNT(*) FROM VesselProfile v WHERE v.updateTime IS NOT NULL AND v.updateTime < :endTime AND v.updateTime >= :startTime")
    int findUpdateCount(@Param("endTime")Date endTime, @Param("startTime")Date startTime);

    @Query("SELECT v FROM VesselProfile v " +
           "WHERE v.updateTime IS NOT NULL AND v.updateTime >= :startTime AND v.updateTime < :endTime " +
           "ORDER BY v.updateTime ASC")
    List<VesselProfile> findVesselUpdateTimeBetween(@Param("startTime")Date startTime, @Param("endTime")Date endTime, Pageable pageable);

    @Query("SELECT MAX(v.updateTime) FROM VesselProfile v")
    Date findMaxUpdateTime();

    @Modifying
    @Query("DELETE FROM VesselProfile v WHERE v.updateTime IS NOT NULL AND v.updateTime < :date")
    int deleteProfileOlderThan(@Param("date") Date date);
}

