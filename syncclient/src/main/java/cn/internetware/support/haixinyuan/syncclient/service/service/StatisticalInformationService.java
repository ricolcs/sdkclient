package cn.internetware.support.haixinyuan.server.service;

import cn.internetware.support.haixinyuan.common.dao.VesselPositionCurrentDao;
import cn.internetware.support.haixinyuan.common.dao.VesselPositionDao;
import cn.internetware.support.haixinyuan.common.dao.VesselProfileDao;
import cn.internetware.support.haixinyuan.server.dao.StatisticalInformationDao;
import cn.internetware.support.haixinyuan.server.model.StatisticalInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gongjianfei on 2017/4/19.
 */
@Service
public class StatisticalInformationService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(StatisticalInformationService.class);
    @Autowired
    private StatisticalInformationDao informationDao;

    @Autowired
    private VesselProfileDao vesselDao;


    @Autowired
    private VesselPositionDao positionDao;

    @Autowired
    private VesselPositionCurrentDao currentDao;


    public void countNum(Date nowTime, Date oneHourAgoTime) {

    }

    public int getVesselTotal() {
        return vesselDao.findAllCount();
    }


    public int findCreateCount(Date nowTime, Date time) {
        return vesselDao.findCreateCount(nowTime, time);
    }

    public int findUpdateCount(Date nowTime, Date time) {
        return vesselDao.findUpdateCount(nowTime, time);
    }

    public int findPositionCreateCount(Date nowTime, Date time) {
        return positionDao.findPositionCreateCount(nowTime, time);
    }

    public int findVesselCount(Date nowTime, Date time) {
        return currentDao.findVesselCreateCount(nowTime, time);
    }

    public List<StatisticalInformation> getStatisticalInfo() {
        List<StatisticalInformation> infoList = (List<StatisticalInformation>) informationDao.findAll();
        Collections.sort(infoList);
        return infoList;
    }

    public List<StatisticalInformation> getStatisticalInfoAccordingDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return getStatisticalInfoAccordingTime(formatter);
    }

    public List<StatisticalInformation> getStatisticalInfoAccordingMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return getStatisticalInfoAccordingTime(formatter);
    }

    public List<StatisticalInformation> getStatisticalInfoAccordingTime(SimpleDateFormat formatter) {
        List<StatisticalInformation> infoList = (List<StatisticalInformation>) informationDao.findAll();
        List<StatisticalInformation> newList = new ArrayList<StatisticalInformation>();

        Map<Date, List<StatisticalInformation>> infoMap = infoList.stream().map(i -> {
            String dateString = formatter.format(i.getStatisticalTime());
            try {
                i.setStatisticalTime(formatter.parse(dateString));
            } catch (ParseException e) {
                LOGGER.error("Time conversion error:{}",e.getMessage());
            }
            return i;
        }).collect(Collectors.groupingBy(i -> i.getStatisticalTime()));
        //Map<Date, List<StatisticalInformation>> infoMap = infoList.stream().collect(Collectors.groupingBy(i -> i.getStatisticalTime()));

        for (Date d : infoMap.keySet()) {
            List<StatisticalInformation> ll = infoMap.get(d);
            int a = ll.stream().max(Comparator.comparing(i -> i.getVesselTotal())).get().getVesselTotal();
            int b = ll.stream().map(i -> i.getVesselCreateTotal()).reduce(0, (acc, element) -> acc + element);
            int c = ll.stream().map(i -> i.getVesselUpdateTotal()).reduce(0, (acc, element) -> acc + element);
            int e = ll.stream().map(i -> i.getVesselPositionCreateTotal()).reduce(0, (acc, element) -> acc + element);
            int f = ll.stream().map(i -> i.getVesselPositionUpdateTotal()).reduce(0, (acc, element) -> acc + element);
            StatisticalInformation info = new StatisticalInformation();
            info.setStatisticalTime(d);
            info.setVesselTotal(a);
            info.setVesselCreateTotal(b);
            info.setVesselUpdateTotal(c);
            info.setVesselPositionCreateTotal(e);
            info.setVesselPositionUpdateTotal(f);
            newList.add(info);
        }
        Collections.sort(newList);
        return newList;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalInfromation() {
        LOGGER.info("Begin to statistical vessel information every hour.");
        Date nowTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        StatisticalInformation info = new StatisticalInformation();
        info.setStatisticalTime(nowTime);
        info.setVesselTotal(vesselDao.findAllCount());
        info.setVesselCreateTotal(vesselDao.findCreateCount(nowTime, calendar.getTime()));
        info.setVesselUpdateTotal(vesselDao.findUpdateCount(nowTime, calendar.getTime()));
        info.setVesselPositionUpdateTotal(currentDao.findVesselCreateCount(nowTime, calendar.getTime()));
        info.setVesselPositionCreateTotal(positionDao.findPositionCreateCount(nowTime, calendar.getTime()));
        informationDao.save(info);
    }
}
