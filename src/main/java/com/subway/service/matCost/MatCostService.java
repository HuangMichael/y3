package com.subway.service.matCost;

import com.subway.dao.app.resource.ResourceRepository;
import com.subway.dao.macCost.MatCostRepository;
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
public class MatCostService extends BaseService {

    @Autowired
    public ResourceRepository resourceRepository;

    @Autowired
    public MatCostRepository matCostRepository;

    /**
     * @return 返回有物资消耗单据的所有的位置
     */
    public List<String> findMyLocs() {
        return matCostRepository.findMyLocs();
    }


}
