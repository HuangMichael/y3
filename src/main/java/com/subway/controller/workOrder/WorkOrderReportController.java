package com.subway.controller.workOrder;


import com.subway.controller.common.BaseController;
import com.subway.domain.app.MyPage;
import com.subway.domain.user.User;
import com.subway.domain.workOrder.VworkOrderNumFinish;
import com.subway.domain.workOrder.VworkOrderNumReport;
import com.subway.domain.workOrder.VworkOrderReportBill;
import com.subway.domain.workOrder.WorkOrderReportCart;
import com.subway.service.app.ResourceService;
import com.subway.service.workOrder.WorkOrderReportCartService;
import com.subway.service.workOrder.WorkOrderReportService;
import com.subway.utils.PageUtils;
import com.subway.utils.SessionUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbin on 2015/12/23 0023.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/workOrderReport")
@Data
public class WorkOrderReportController extends BaseController {


    @Autowired
    WorkOrderReportService workOrderReportService;

    @Autowired
    WorkOrderReportCartService workOrderReportCartService;

    @Autowired
    ResourceService resourceService;


    /**
     * 分页查询
     *
     * @param request
     * @param current      当前页
     * @param rowCount     每页条数
     * @param searchPhrase 查询关键字
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(HttpSession session, HttpServletRequest request, @RequestParam(value = "current", defaultValue = "0") int current, @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount, @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        String location = SessionUtil.getCurrentUserLocationBySession(session);
        if (searchPhrase != null && !searchPhrase.equals("")) {
            searchPhrase = location + "," + searchPhrase;
        } else {
            searchPhrase = location + ",,,,,";
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(workOrderReportService, searchPhrase, 5, current, rowCount, pageable);
    }


    /**
     * 批量更新设备故障描述
     */
    @RequestMapping(value = "/generateReport", method = RequestMethod.POST)
    @ResponseBody
    public List<WorkOrderReportCart> generateReport(@RequestParam("ids") String ids, HttpSession session) {
        User user = SessionUtil.getCurrentUserBySession(session);
        List<WorkOrderReportCart> workOrderReportDetailList = new ArrayList<WorkOrderReportCart>();
        if (user != null && user.getVlocations().getLocation() != null && user.getPerson() != null) {
            workOrderReportDetailList = workOrderReportService.generateReport(ids);
        }
        return workOrderReportDetailList;

    }


    /**
     * @return 按照设备分类进行规约
     * //生成历史信息  并且更新状态
     */
    @RequestMapping(value = "/mapByUnitId", method = RequestMethod.POST)
    @ResponseBody
    public List mapByUnitId(@RequestParam("ids") String ids) {
        return workOrderReportService.mapByUnitId(ids);
    }

    /**
     * @return 查询近期三个月的报修单
     */
    @RequestMapping(value = "/sel3mRptNum", method = RequestMethod.GET)
    @ResponseBody
    public List<VworkOrderNumReport> selectReportNumIn3Months() {
        return workOrderReportService.selectReportNumIn3Months();
    }

    /**
     * @return 查询近期三个月的报修单
     */
    @RequestMapping(value = "/sel3mFinishNum", method = RequestMethod.GET)
    @ResponseBody
    public List<VworkOrderNumFinish> selectFinishNumIn3Months() {
        return workOrderReportService.selectFinishNumIn3Months();
    }


    /**
     * @param request  请求
     * @param response 响应
     * @param param    查询关键字
     * @param docName  文档名称
     * @param titles   标题集合
     * @param colNames 列名称
     */
    @ResponseBody
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam("param") String param, @RequestParam("docName") String docName, @RequestParam("titles") String titles[], @RequestParam("colNames") String[] colNames) {
        List<VworkOrderReportBill> dataList = workOrderReportService.findByConditions(param, 4);
        workOrderReportService.setDataList(dataList);
        workOrderReportService.exportExcel(request, response, docName, titles, colNames);
    }
}
