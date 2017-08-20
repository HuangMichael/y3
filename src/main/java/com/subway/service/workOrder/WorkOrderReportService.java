package com.subway.service.workOrder;

import com.subway.dao.equipments.EquipmentsClassificationRepository;
import com.subway.dao.equipments.EquipmentsRepository;
import com.subway.dao.locations.LocationsRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.dao.workOrder.*;
import com.subway.domain.equipments.Equipments;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.locations.Locations;
import com.subway.domain.units.Units;
import com.subway.domain.workOrder.*;
import com.subway.service.app.BaseService;
import com.subway.utils.LocationSeparatable;
import com.subway.utils.StringUtils;
import com.subway.utils.search.SortedSearchable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangbin  on 2016/5/20.
 */
@Service
public class WorkOrderReportService extends BaseService implements SortedSearchable {


    @Autowired
    EquipmentsRepository equipmentsRepository;

    @Autowired
    WorkOrderReportCartRepository workOrderReportCartRepository;

    @Autowired
    OutsourcingUnitRepository outsourcingUnitRepository;

    @Autowired
    VlocationsRepository vlocationsRepository;
    @Autowired
    VworkOrderNumReportRepository vworkOrderNumReportRepository;

    @Autowired
    VworkOrderNumFinishRepository vworkOrderNumFinishRepository;

    @Autowired
    WorkOrderHistoryRepository workOrderHistoryRepository;

    @Autowired
    WorkOrderFixService workOrderFixService;


    @Autowired
    VworkOrderReportBillRepository vworkOrderReportBillRepository;

    @Autowired
    LocationsRepository locationsRepository;

    @Autowired
    EquipmentsClassificationRepository equipmentsClassificationRepository;

    /**
     * @param ids 选中的报修车列表id集合
     * @return
     */

    public List<WorkOrderReportCart> generateReport(String ids) {
        List<Long> idList = new ArrayList<Long>();
        if (ids != null && !ids.equals("")) {
            idList = StringUtils.str2List(ids, ",");
        }
        List<WorkOrderReportCart> workOrderReportCartList = new ArrayList<WorkOrderReportCart>();
        WorkOrderReportCart workOrderReportCart;
        for (Long id : idList) {
            workOrderReportCart = workOrderReportCartRepository.findById(id);
            workOrderReportCart.setNodeState("已报修");
            workOrderReportCart.setDeadLine(workOrderFixService.getDeadLineByEqClass(workOrderReportCart));
            workOrderReportCart.setLastStatusTime(new Date());//更新最新状态时间字段
            workOrderReportCart.setStatus("1");
            workOrderReportCartRepository.save(workOrderReportCart);
            workOrderFixService.updateNodeStatus(workOrderReportCart);
            WorkOrderHistory workOrderHistory = new WorkOrderHistory();
            workOrderHistory.setNodeDesc("已报修");
            workOrderHistory.setNodeTime(new Date());
            workOrderHistory.setStatus("1");
            workOrderHistory.setWorkOrderReportCart(workOrderReportCart);
            workOrderHistoryRepository.save(workOrderHistory);
        }
        return workOrderReportCartList;
    }


    /**
     * @return 根据维修单位Id规约
     * @version 0.1
     */

    public List mapByUnitId(String ids) {
        List<WorkOrderReportCart> workOrderReportCartList = new ArrayList<WorkOrderReportCart>();
        List<Long> idsList = StringUtils.str2List(ids, ",");
        for (Long id : idsList) {
            WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(id);
            workOrderReportCart.setNodeState("已派工");
            workOrderReportCart = setDefaultUnit(workOrderReportCart);//设置默认的维修单位
            workOrderReportCart.setLastStatusTime(new Date());//加入最新状态时间
            workOrderReportCart = workOrderReportCartRepository.save(workOrderReportCart);
            workOrderFixService.updateNodeStatus(workOrderReportCart);
            //将已报修状态修改为0
            WorkOrderHistory workOrderHistory = new WorkOrderHistory();
            workOrderHistory.setNodeDesc("已派工");
            workOrderHistory.setNodeTime(new Date());
            workOrderHistory.setStatus("1");
            workOrderHistory.setWorkOrderReportCart(workOrderReportCart);
            workOrderHistoryRepository.save(workOrderHistory);
            workOrderReportCart = workOrderReportCartRepository.save(workOrderReportCart);
            workOrderReportCartList.add(workOrderReportCart);
        }
        return workOrderReportCartList;

    }

    /**
     * @param equipmentsClassification
     * @return 根据设备分类信息获取维修单位
     */
    public Units getDefaultUnitByEqClass(EquipmentsClassification equipmentsClassification) {
        List<Units> unitList = equipmentsClassification.getUnitSet();
        if (!unitList.isEmpty()) {
            return unitList.get(0);
        } else {
            return outsourcingUnitRepository.findAll().get(0);
        }
    }


    /**
     * @return 查询近期三个月的报修单数量
     */
    //  * @param location 查询当前用户的位置下的报修数量统计
    public List<VworkOrderNumReport> selectReportNumIn3Months() {
        return vworkOrderNumReportRepository.findAll();
    }

    /**
     * @return 查询近期三个月的报修单数量
     */
    //  * @param location 查询当前用户的位置下的报修数量统计
    public List<VworkOrderNumFinish> selectFinishNumIn3Months() {
        return vworkOrderNumFinishRepository.findAll();
    }


    public WorkOrderReportCart setDefaultUnit(WorkOrderReportCart workOrderReportCart) {
        EquipmentsClassification equipmentsClassification = workOrderReportCart.getEquipmentsClassification();

        if (equipmentsClassification != null) {
            workOrderReportCart.setUnit(getDefaultUnitByEqClass(equipmentsClassification));
        }
        return workOrderReportCart;
    }


    /**
     * @param searchPhrase 查询关键字
     * @param pageable     可分页
     * @return 根据条件查询
     */
    public Page<VworkOrderReportBill> findByConditions(String searchPhrase, int paramsSize, Pageable pageable) {
        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        return vworkOrderReportBillRepository.findByLocationContainsAndOrderLineNoContainsAndOrderDescContainsAndLocNameContainsAndEqClassContainsOrderByNodeTimeDesc(array[0], array[1], array[2], array[3], array[4], pageable);
    }


    /**
     * @param searchPhrase 查询关键字
     * @return 根据条件查询
     */
    public List<VworkOrderReportBill> findByConditions(String searchPhrase, int paramsSize) {

        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        return vworkOrderReportBillRepository.findByLocationContainsAndOrderLineNoContainsAndOrderDescContainsAndLocNameContainsAndEqClassContainsOrderByNodeTimeDesc(array[0], array[1], array[2], array[3], array[4]);

    }


    /**
     * @param eid
     * @return 根据设备查询报修记录
     */
    public List<WorkOrderReportCart> findByEid(Long eid) {
        Equipments equipments = equipmentsRepository.findById(eid);
        return workOrderReportCartRepository.findByEquipmentsOrderByReportTimeDesc(equipments);
    }


    /**
     * @param lid 位置id
     * @param cid 设备分类id
     * @return 根据设备查询报修记录
     */
    public List<WorkOrderReportCart> findByLidAndEid(Long lid, Long cid) {
        Locations locations = locationsRepository.findById(lid);
        EquipmentsClassification equipmentsClassification = equipmentsClassificationRepository.findById(cid);
        return workOrderReportCartRepository.findByLocationsAndEquipmentsClassificationOrderByReportTimeDesc(locations, equipmentsClassification);
    }
}
