package com.subway.service.org;

import com.subway.dao.app.org.OrgRepository;
import com.subway.dao.macCost.MatCostRepository;
import com.subway.domain.app.org.SystemInfo;
import com.subway.service.app.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangbin
 * 2016年9月29日10:03:31
 * <p/>
 * 物料消耗查询业务类
 */
@Service
public class SysInfoService extends BaseService {

    @Autowired
    public OrgRepository orgRepository;

    @Autowired
    public MatCostRepository matCostRepository;


    /**
     * @param sysName 系统参数名称
     * @return
     */
    public SystemInfo findBySysName(String sysName) {
        List<SystemInfo> systemInfoList = orgRepository.findBySysName(sysName);
        return (systemInfoList != null) ? systemInfoList.get(0) : null;
    }
}
