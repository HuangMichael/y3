package com.subway.tasks;

import com.subway.domain.workOrder.WorkOrderReportCart;
import com.subway.service.workOrder.WorkOrderReportCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 维修任务状态定时更新
 *
 * @author huangbin
 * @create 2016-09-05 14:08
 **/
@Component
public class SetWorkOrderExpiredTask {

    private static final Logger log = LoggerFactory.getLogger(SetWorkOrderExpiredTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    WorkOrderReportCartService workOrderReportCartService;


    //每天上午十点执行
    @Scheduled(cron = "0 0 10 * * *")
    public void updateFixTaskStatus() {
        List<WorkOrderReportCart> workOrderReportCartList = workOrderReportCartService.findBeingExpired();
        int num = workOrderReportCartService.handleExpiredOrders(workOrderReportCartList);
        log.info("定时更新工单过期状态---- {}----------" + num + "条", dateFormat.format(new Date()));
    }
}
