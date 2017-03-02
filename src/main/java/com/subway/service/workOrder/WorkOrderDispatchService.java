package com.subway.service.workOrder;

import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.dao.workOrder.WorkOrderReportCartRepository;
import com.subway.domain.units.Units;
import com.subway.domain.workOrder.WorkOrderReportCart;
import com.subway.service.app.BaseService;
import com.subway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin  on 2016/5/20.
 * 工单调度信息业务类
 */
@Service
public class WorkOrderDispatchService extends BaseService {

    @Autowired
    OutsourcingUnitRepository outsourcingUnitRepository;

    @Autowired
    WorkOrderReportCartRepository workOrderReportCartRepository;


    /**
     * @param detailId
     * @param unitId
     * @return 返回更新了维修单位的报修明细信息
     */
    @Transactional
    public WorkOrderReportCart updateDetailUnit(Long detailId, Long unitId) {
        WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(detailId);
        Units outsourcingUnit = outsourcingUnitRepository.findById(unitId);
        if (workOrderReportCart != null && outsourcingUnit != null) {
            workOrderReportCart.setUnit(outsourcingUnit);
            workOrderReportCart.setNodeState("已派工");
            workOrderReportCart = workOrderReportCartRepository.save(workOrderReportCart);
        }
        return workOrderReportCart;
    }


    /**
     * @param ids
     * @return 根据id查询维修车信息集合
     */
    public List<WorkOrderReportCart> findWorkOrderReportCartByIds(String ids) {
        List<WorkOrderReportCart> workOrderReportCartList = new ArrayList<WorkOrderReportCart>();
        List<Long> longList = StringUtils.str2List(ids, ",");
        for (int i = 0; i < longList.size(); i++) {
            WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(longList.get(i));
            workOrderReportCartList.add(workOrderReportCart);
        }
        return workOrderReportCartList;
    }
}
