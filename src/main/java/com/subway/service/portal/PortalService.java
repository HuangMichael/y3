package com.subway.service.portal;

/**
 * Created by Administrator on 2016/7/24.
 */

import com.subway.dao.line.LineRepository;
import com.subway.dao.workOrder.VlineMonthRepository;
import com.subway.dao.workOrder.VworkOrderLineCompareRepository;
import com.subway.dao.workOrder.VworkOrderStationCompareRepository;
import com.subway.dao.workOrder.WorkOrderReportCartRepository;
import com.subway.domain.workOrder.VlineMonth;
import com.subway.domain.workOrder.VworkOrderLineCompare;
import com.subway.domain.workOrder.VworkOrderStationCompare;
import com.subway.domain.workOrder.VworkOrderStationCompareVo;
import com.subway.object.statistics.ValueObject;
import com.subway.service.app.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbin on 2016/7/24.
 */
@Service
public class PortalService extends BaseService {


    @Autowired
    VlineMonthRepository vlineMonthRepository;
    @Autowired
    WorkOrderReportCartRepository workOrderReportCartRepository;
    @Autowired
    VworkOrderLineCompareRepository vworkOrderLineCompareRepository;
    @Autowired
    VworkOrderStationCompareRepository vworkOrderStationCompareRepository;

    @Autowired
    LineRepository lineRepository;

    /**
     * @param reportMonth 月份
     * @param name        工单状态
     * @return 获取线路统计汇总
     */
    public List<VlineMonth> getLineReportNumReportMonth(String reportMonth, String name) {


        log.info("getLineReportNumReportMonth----------------" + reportMonth + "---name" + name);

        return vlineMonthRepository.findByReportMonthAndName(reportMonth, name);
    }

    /**
     * @param reportMonth
     * @return 设备分类按照数量排序
     */
    public List<Object> findTopNReportByEqClass(String reportMonth) {
        List<Object> dataList = workOrderReportCartRepository.findTopNReportByEqClass(reportMonth);
        return dataList;

    }


    /**
     * @param reportMonth 报修月份
     * @param status      工单状态
     * @return
     */
    public List<VworkOrderLineCompare> findByReportMonthAndStatusOrderByNameAsc(String reportMonth, String status) {

        return vworkOrderLineCompareRepository.findByReportMonthAndStatusOrderByNameAsc(reportMonth, status);
    }


    /**
     * @param reportMonth 报修月份
     * @param status      工单状态
     * @return
     */
    public List<VworkOrderStationCompareVo> findByReportMonthAndStatusOrderByStationNoAsc(String reportMonth, String status) {
        //先查询几个线路编号
        List<String> linesStr = lineRepository.findLineNos();
        List<VworkOrderStationCompare> vworkOrderStationCompares;
        List<VworkOrderStationCompareVo> vworkOrderStationComparesList = new ArrayList<VworkOrderStationCompareVo>();
        VworkOrderStationCompareVo vworkOrderStationCompareVo;
        for (String line : linesStr) {
            vworkOrderStationCompares = vworkOrderStationCompareRepository.findByLineNoAndReportMonthAndNameOrderByStationNoAsc(line, reportMonth, status);
            vworkOrderStationCompareVo = new VworkOrderStationCompareVo();
            vworkOrderStationCompareVo.setId(line);
            vworkOrderStationCompareVo.setName(line);
            List<ValueObject> list = new ArrayList<ValueObject>();
            for (VworkOrderStationCompare v : vworkOrderStationCompares) {
                ValueObject valueObject = new ValueObject();
                valueObject.setStr(v.getStation());
                valueObject.setValue(v.getNum());
                list.add(valueObject);
            }

            vworkOrderStationCompareVo.setData(list);
            vworkOrderStationComparesList.add(vworkOrderStationCompareVo);
        }
        return vworkOrderStationComparesList;
    }


}
