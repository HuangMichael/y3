package com.subway.dao.workOrder;

import com.subway.domain.workOrder.VworkOrderLineCompare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by huangbin on 2017/5/13.
 */
public interface VworkOrderLineCompareRepository extends JpaRepository<VworkOrderLineCompare, Long> {

    /**
     * @param reportMonth 报修日期
     * @param status      工单状态
     * @return
     */
    List<VworkOrderLineCompare> findByReportMonthAndStatusOrderByNameAsc(String reportMonth, String status);
}
