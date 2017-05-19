package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselPhoto;
import cn.internetware.support.haixinyuan.common.model.VesselProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface VesselPhotoDao extends CrudRepository<VesselPhoto, Long> {
    @Query("SELECT vp " +
            "FROM VesselProfile vp " +
            "WHERE vp.photo IS NOT NULL AND vp.photo NOT IN (SELECT photo.id FROM VesselPhoto photo)")
    List<VesselProfile> findVesselToUpdatePhoto(Pageable pageable);

    @Query("SELECT vp FROM VesselPhoto vp " +
            "WHERE vp.crawlTime IS NOT NULL AND vp.crawlTime >= :startTime AND vp.crawlTime < :endTime " +
            "ORDER BY vp.crawlTime ASC")
    List<VesselPhoto> findPhotoCrawlTimeBetween(
            @Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable pageable);

    @Query("SELECT MAX(vp.crawlTime) FROM VesselPhoto vp")
    Date findMaxCrawlTime();
}

