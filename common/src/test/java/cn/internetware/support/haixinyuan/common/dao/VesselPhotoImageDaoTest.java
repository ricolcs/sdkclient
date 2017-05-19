package cn.internetware.support.haixinyuan.common.dao;

import cn.internetware.support.haixinyuan.common.model.VesselPhoto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class VesselPhotoImageDaoTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(VesselPhotoImageDaoTest.class);

    @Test
    public void testGetStorePath() throws Exception {
        VesselPhoto photo = new VesselPhoto(22263L, 0L);
        LOGGER.info("Get stored path {}", VesselPhotoImageDao.getStorePath(photo));
    }
}