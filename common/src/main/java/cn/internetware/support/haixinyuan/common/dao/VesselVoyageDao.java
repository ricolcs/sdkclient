package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import cn.internetware.support.haixinyuan.common.model.VesselVoyage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface VesselVoyageDao extends CrudRepository<VesselVoyage, Long> {
    @Query("SELECT vp " +
            "FROM VesselProfile vp, VesselPositionCurrent vpc, VesselVoyage vv " +
            "WHERE (vp.shipid = vpc.shipid AND vpc.shipid = vv.shipid AND vpc.time IS NOT NULL) AND (" +
            "          vv.crawlTime IS NULL OR " +
            "          (vv.crawlTime < vpc.time AND unix_timestamp(vv.crawlTime) - unix_timestamp(current_timestamp) < -9000)" +
            "      ) " +
            "ORDER BY vv.crawlTime ASC")
    List<VesselProfile> findVesselToUpdateVoyage(Pageable pageable);

    @Query("SELECT vv FROM VesselVoyage vv " +
            "WHERE vv.crawlTime IS NOT NULL AND vv.crawlTime >= :timeStart AND vv.crawlTime < :timeEnd")
    List<VesselVoyage> findVoyageUpdateTimeBetween(@Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd);

    @Modifying
    @Query("DELETE FROM VesselVoyage vv WHERE vv.crawlTime IS NOT NULL AND vv.crawlTime < :date")
    int deleteVoyageOlderThan(@Param("date") Date sevenDayAgo);
}

