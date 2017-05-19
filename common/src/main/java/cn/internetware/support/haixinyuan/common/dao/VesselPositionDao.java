package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface VesselPositionDao extends CrudRepository<VesselPosition, Long> {
    List<VesselPosition> findByShipid(Long shipid);

    @Query("SELECT p from VesselPosition p where p.id >= :startId")
    Page<VesselPosition> findPositionListLatest(@Param("startId") long startId, Pageable pageable);

    @Query("SELECT count(*) from VesselPosition v where v.createTime < :endTime and v.createTime >= :startTime")
    int findPositionCreateCount(@Param("endTime") Date endTime, @Param("startTime") Date startTime);

    @Query("SELECT v FROM VesselPosition v " +
           "WHERE v.id >= :startPosition " +
           "ORDER BY v.id ASC")
    List<VesselPosition> findByStartPosition(
            @Param("startPosition") long startPosition, Pageable pageable);

    @Query("SELECT MAX(p.id) FROM VesselPosition p")
    Long findMaxId();

    @Modifying
    @Query("DELETE FROM VesselPosition p WHERE p.id <:eventPosition")
    int deletePositionOlderThan(@Param("eventPosition") Long eventPosition);
}
