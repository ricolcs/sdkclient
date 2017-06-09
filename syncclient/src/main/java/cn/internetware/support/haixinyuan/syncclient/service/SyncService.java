package cn.internetware.support.haixinyuan.syncclient.service;

import cn.internetware.support.haixinyuan.common.dao.*;
import cn.internetware.support.haixinyuan.common.model.*;
import cn.internetware.support.haixinyuan.common.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Service
public class SyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncService.class);
    private static long MILISECONDS_OF_7_DAYS = 7 * 24 * 3600 * 1000; // 7 days.
    private static long POSITION_RECORD_KEEP_COUNT = 10000 * 10000; // 100 millions.
    private static long VOYAGE_RECORD_KEEP_COUNT = 1000 * 10000; // 100 millions.

    @Autowired
    private VesselProfileDao vesselProfileDao;

    @Autowired
    private VesselPhotoDao vesselPhotoDao;

    @Autowired
    private VesselPhotoImageDao vesselPhotoImageDao;

    @Autowired
    private VesselVoyageDao vesselVoyageDao;

    @Autowired
    private VesselVoyageHistoryDao vesselVoyageHistoryDao;

    @Autowired
    private VesselPositionDao vesselPositionDao;

    @Autowired
    private VesselPositionCurrentDao vesselPositionCurrentDao;

    private SyncRecord current;

    public SyncService() {
    }

    protected synchronized SyncRecord getCurrentSyncRecord() {
        if (current == null) {
            current = new SyncRecord(0L, vesselProfileDao.findMaxUpdateTime(), vesselPhotoDao.findMaxCrawlTime(), vesselPositionDao.findMaxId());
            if (current.profileTime == null) {
                current.profileTime = new Date(0L);
            }
            if (current.photoTime == null) {
                current.photoTime = new Date(0L);
            }
            if (current.eventPosition == null) {
                current.eventPosition = -1L; // Fetch server's max id.
            }
        }
        return current;
    }

    @Scheduled(cron = "0 0 * * * *")
    // @Scheduled(fixedDelay = 1000L)
    public void deleteOldData() {
        LOGGER.info("Begin to delete old data.");
        SyncRecord currentSyncRecord = getCurrentSyncRecord();
        Date sevenDayAgo = TimeUtil.getDate(Math.max(0L, currentSyncRecord.profileTime.getTime() - MILISECONDS_OF_7_DAYS));

        LOGGER.info("Deleting vessel profiles older than 7 days, sevenDayAgo={}.", TimeUtil.formatDate(sevenDayAgo));
        int vesselProfileDeleted = vesselProfileDao.deleteProfileOlderThan(sevenDayAgo);
        LOGGER.info("Total {} vessel profile records deleted.", vesselProfileDeleted);

        LOGGER.info("Delete vessel voyage older than 7 days, sevenDayAgo={}.", TimeUtil.formatDate(sevenDayAgo));
        int vesselVoyageDeleted = vesselVoyageDao.deleteVoyageOlderThan(sevenDayAgo);
        LOGGER.info("Total {} vessel voyage records deleted.", vesselVoyageDeleted);

        LOGGER.info("Delete vessel position current records older than 7 days, sevenDayAgo={}.", TimeUtil.formatDate(sevenDayAgo));
        int vesselPositionCurrentDeleted = vesselPositionCurrentDao.deletePositionOlderThan(sevenDayAgo);
        LOGGER.info("Total {} vessel position current records deleted.", vesselPositionCurrentDeleted);

        Long minEventPosition = Math.max(0L, currentSyncRecord.eventPosition - POSITION_RECORD_KEEP_COUNT);
        LOGGER.info("Delete vessel position before 100 millions ago, minEventPosition={}", minEventPosition);
        int vesselPositionDeleted = vesselPositionDao.deletePositionOlderThan(minEventPosition);
        LOGGER.info("Total {} vessel position record deleted.", vesselPositionDeleted);

        Long voyageMinId = vesselVoyageHistoryDao.findMaxId();
        if (voyageMinId != null) {
            voyageMinId = Math.max(voyageMinId - VOYAGE_RECORD_KEEP_COUNT, 0L);
        } else {
            voyageMinId = 0L;
        }
        LOGGER.info("Delete vessel voyage position before 10 millions ago, minEventPosition={}", voyageMinId);
        int voyageHistoryDeleted = vesselVoyageHistoryDao.deleteVoyageRecordLessThan(voyageMinId);
        LOGGER.info("Total {} vessel voyage history record deleted.", voyageHistoryDeleted);

        LOGGER.info("Old data deleted.");
    }

    @Scheduled(fixedDelay = 10000L)
    public void runSyncByPush() {
        DataSyncClient client;
        try {
            client = new DataSyncClient();
        } catch (Exception e) {
            LOGGER.error("Error while create data sync client: ", e);
            return;
        }
        while (client.checkConnection()) {
            TimeUtil.sleepForTime(10000L);
        }
    }

    @Transactional
    protected SyncRequest processSyncResponse(SyncRecord currentSyncRecord, SyncRequest request) {
        LOGGER.info("Process sync response: {}", request);
        Date currentTime = new Date();
        if (request.vesselProfileList != null) {
            for (VesselProfile profile : request.vesselProfileList) {
                profile.setSendTime(currentTime);
            }
            vesselProfileDao.save(request.vesselProfileList);
        }

        if (request.vesselVoyageList != null) {
            for (VesselVoyage voyage : request.vesselVoyageList) {
                voyage.setSendTime(currentTime);
                VesselVoyageHistory history = new VesselVoyageHistory();
                BeanUtils.copyProperties(voyage, history);
                vesselVoyageHistoryDao.save(history);
            }
            vesselVoyageDao.save(request.vesselVoyageList);
        }

        if (request.vesselPhotoList != null) {
            for (VesselPhoto photo : request.vesselPhotoList) {
                photo.setSendTime(currentTime);
            }
            vesselPhotoDao.save(request.vesselPhotoList);
            vesselPhotoImageDao.saveImageForPhotoList(request.vesselPhotoList);
        }

        if (request.vesselPositionList != null) {
            for (VesselPosition position : request.vesselPositionList) {
                position.setSendTime(currentTime);
                VesselPositionCurrent current = new VesselPositionCurrent();
                BeanUtils.copyProperties(position, current);

                vesselPositionDao.save(position);
                vesselPositionCurrentDao.save(current);
            }
        }

        if (request.profileTimeEnd != null) {
            currentSyncRecord.profileTime = new Date(request.profileTimeEnd);
        }
        if (request.photoTimeEnd != null) {
            currentSyncRecord.photoTime = new Date(request.photoTimeEnd);
        }
        if (request.eventPositionEnd != null) {
            currentSyncRecord.eventPosition = request.eventPositionEnd;
        }
        LOGGER.info("Process sync response finished.");
        return request;
    }

    @ClientEndpoint
    protected class DataSyncClient {
        private ObjectMapper mapper = new ObjectMapper();
        private Session session;
        private volatile long lastSendTime;

        public DataSyncClient() {
            LOGGER.info("Begin to create data sync client.");
            String syncUriString = String.format("ws://%s/ws/sync", System.getProperty("haixinyuan.syncserver", "112.126.87.36:28080"));
            URI syncUri = null;
            try {
                syncUri = new URI(syncUriString);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Illegal sync uri: " + syncUriString, e);
            }

            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.setDefaultMaxTextMessageBufferSize(50 * 1024 * 1024);
                session = container.connectToServer(this, syncUri);
                lastSendTime = System.currentTimeMillis();
            } catch (Exception e) {
                LOGGER.error("Create websocket link to server uri={} failed.", syncUri, e);
                throw new RuntimeException("Create DataSyncClient failed.", e);
            }
            LOGGER.info("Data sync client created ok.");
        }

        @OnOpen
        public void onOpen(Session session, EndpointConfig config) {
            LOGGER.info("Session opened, session={}, config={}.", session, config);
            this.session = session;
            sendCurrentSyncRecord();
        }

        @OnMessage
        public void onMessage(Session session, String message) {
            LOGGER.debug("{}: message received: {}", this, message);
            try {
                SyncRequest request = mapper.readValue(message, SyncRequest.class);
                processSyncResponse(getCurrentSyncRecord(), request);
                sendCurrentSyncRecord();
            } catch (IOException e) {
                LOGGER.error("Process server response failed: ", e);
                close();
            }
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            LOGGER.error("Error occured in session={}, close the connection.", session, throwable);
            close();
        }

        @OnClose
        public void onClose(Session session, CloseReason closeReason) {
            LOGGER.error("Websocket client onClose called, session={}, reason={}", session, closeReason);
        }

        public boolean checkConnection() {
            if (System.currentTimeMillis() - lastSendTime < 180000) {
                return true;
            } else  {
                LOGGER.warn("Server failed to response in 180 seconds, close it and make connection again.");
                close();
                return false;
            }
        }

        protected void sendCurrentSyncRecord() {
            SyncRecord syncRecord = getCurrentSyncRecord();
            try {
                session.getBasicRemote().sendText(mapper.writeValueAsString(syncRecord));
                lastSendTime = System.currentTimeMillis();
                LOGGER.info("Send sync record ok: {}", syncRecord);
            } catch (Exception e) {
                LOGGER.error("Send sync record failed, will close connection.", e);
                close();
            }
        }

        protected void close() {
            LOGGER.info("Begin to close the connection.");
            try {
                session.close();
                LOGGER.info("Close connection success.");
            } catch (Exception e) {
                LOGGER.error("Close session failed:", e);
            }
        }
    }
}

