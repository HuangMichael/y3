package com.subway.dao.workOrder;

import com.subway.domain.workOrder.VworkOrderStationCompare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by huangbin on 2017/5/13.
 */
public interface VworkOrderStationCompareRepository extends JpaRepository<VworkOrderStationCompare, Long> {

    /**
     * @param reportMonth 报修日期
     * @param status      工单状态
     * @return
     */
    List<VworkOrderStationCompare> findByReportMonthAndNameOrderByStationNoAsc(String reportMonth, String status);

    /**
     * @param lineNo
     * @param reportMonth
     * @param status
     * @return
     */
    List<VworkOrderStationCompare> findByLineNoAndReportMonthAndNameOrderByStationNoAsc(String lineNo,String reportMonth, String status);
}
