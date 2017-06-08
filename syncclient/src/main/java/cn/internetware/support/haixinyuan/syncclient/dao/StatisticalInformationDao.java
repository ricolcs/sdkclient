package cn.internetware.support.haixinyuan.syncclient.dao;

import cn.internetware.support.haixinyuan.syncclient.model.StatisticalInformation;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by gongjianfei on 2017/4/19.
 */
@Transactional
public interface StatisticalInformationDao extends CrudRepository<StatisticalInformation, Long> {

}
