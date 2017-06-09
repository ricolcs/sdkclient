package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselVoyageHistory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VesselVoyageHistoryDao extends CrudRepository<VesselVoyageHistory, Long> {
    @Query("SELECT MAX(id) FROM VesselVoyageHistory")
    Long findMaxId();

    @Modifying
    @Query("DELETE FROM VesselVoyageHistory vvh WHERE vvh.id < :input")
    int deleteVoyageRecordLessThan(@Param("input") Long input);
}

