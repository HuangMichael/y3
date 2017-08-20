package com.subway.controller.workOrder;


import com.subway.controller.common.BaseController;
import com.subway.dao.workOrder.WorkOrderHistoryRepository;
import com.subway.dao.workOrder.WorkOrderReportCartRepository;
import com.subway.domain.app.MyPage;
import com.subway.domain.user.User;
import com.subway.domain.workOrder.VworkOrderFixBill;
import com.subway.domain.workOrder.WorkOrderHistory;
import com.subway.domain.workOrder.WorkOrderReportCart;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.workOrder.WorkOrderFixSearchService;
import com.subway.service.workOrder.WorkOrderFixService;
import com.subway.service.workOrder.WorkOrderReportCartService;
import com.subway.service.workOrder.WorkOrderReportService;
import com.subway.utils.DateUtils;
import com.subway.utils.PageUtils;
import com.subway.utils.SessionUtil;
import com.subway.utils.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbin on 2015/12/23 0023.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/workOrderFix")
@Data
public class WorkOrderFixController extends BaseController {
    @Autowired
    CommonDataService commonDataService;

    @Autowired
    WorkOrderReportService workOrderReportService;

    @Autowired
    WorkOrderFixService workOrderFixService;


    @Autowired
    WorkOrderFixSearchService workOrderFixSearchService;

    @Autowired
    WorkOrderHistoryRepository workOrderHistoryRepository;

    @Autowired
    WorkOrderReportCartRepository workOrderReportCartRepository;
    @Autowired
    WorkOrderReportCartService workOrderReportCartService;


    private String location;

    /**
     * @param modelMap
     * @return 显示维修工单列表
     */
    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public String list2(ModelMap modelMap, HttpSession session) {
        User user = SessionUtil.getCurrentUserBySession(session);
        String location = user.getVlocations().getLocation();
        //查询出已派工的维修单*/
        return "/workOrderFix/list";
    }


    /**
     * @param request
     * @param current
     * @param rowCount
     * @param searchPhrase
     * @return 显示维修工单列表
     */

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(HttpSession session, HttpServletRequest request,
                       @RequestParam(value = "current", defaultValue = "0") int current,
                       @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount,
                       @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {

        String location = SessionUtil.getCurrentUserLocationBySession(session);
        if (searchPhrase != null && !searchPhrase.equals("")) {
            searchPhrase = location + "," + searchPhrase;
        } else {
            searchPhrase = location + ",,,,,,,";
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(workOrderFixSearchService, searchPhrase, 6, current, rowCount, pageable);

    }


    /**
     * @param request
     * @param current
     * @param rowCount
     * @param searchPhrase
     * @return 显示维修工单列表
     */

    @RequestMapping(value = "/expiredCount", method = RequestMethod.POST)
    @ResponseBody
    public MyPage expiredCount(HttpSession session, HttpServletRequest request,
                               @RequestParam(value = "current", defaultValue = "0") int current,
                               @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount,
                               @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        String location = SessionUtil.getCurrentUserLocationBySession(session);
        if (searchPhrase != null && !searchPhrase.equals("")) {
            searchPhrase = location + "," + searchPhrase;
        } else {
            searchPhrase = location + ",,,,,,,";
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(workOrderFixSearchService, searchPhrase, 6, current, rowCount, pageable);

    }


    /**
     * @param fixId
     * @return 完成单个维修工单
     */
    @RequestMapping(value = "/finishDetail", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject finishDetail(@RequestParam Long fixId, @RequestParam String fixDesc) {
        WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(fixId);
        WorkOrderHistory workOrderHistory = workOrderFixService.handleWorkOrder(workOrderReportCart, fixDesc, "已完工");
        return commonDataService.getReturnType(workOrderHistory != null, "维修单完工成功!", "维修单完工失败!");
    }


    /**
     * @param fixId   维修单id
     * @param fixDesc 维修描述
     * @return 暂停单个维修工单
     */
    @RequestMapping(value = "/pauseDetail", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject pauseDetail(@RequestParam Long fixId, @RequestParam String fixDesc) {
        WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(fixId);
        WorkOrderHistory workOrderHistory = workOrderFixService.handleWorkOrder(workOrderReportCart, fixDesc, "已暂停");
        return commonDataService.getReturnType(workOrderHistory != null, "维修单暂停成功!", "维修单暂停失败!");
    }

    /**
     * @param fixId
     * @param fixDesc
     * @return 取消单个维修工单
     */
    @RequestMapping(value = "/abortDetail", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject abortDetail(@RequestParam Long fixId, @RequestParam String fixDesc) {
        WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(fixId);
        WorkOrderHistory workOrderHistory = workOrderFixService.handleWorkOrder(workOrderReportCart, fixDesc, "已取消");
        return commonDataService.getReturnType(workOrderHistory != null, "维修单取消成功!", "维修单取消失败!");
    }


    /**
     * @param orderId
     * @return 获取次日零点时间
     */
    @RequestMapping(value = "/getCellingDate/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Date getCellingDate(@PathVariable("orderId") Long orderId) {
        //根据维修单id查询分配订单时间
        Date outDate = null;
        WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(orderId);
        List<WorkOrderHistory> workOrderHistoryList = workOrderHistoryRepository.findByWorkOrderReportCart(workOrderReportCart);
        if (!workOrderHistoryList.isEmpty()) {
            Date dispatchTime = workOrderHistoryList.get(0).getNodeTime();
            outDate = DateUtils.getCellingDate(dispatchTime);
        }
        return outDate;
    }

    /**
     * @param orderId     工单id
     * @param deadLineStr 截止日期字符串
     * @return 更新截止日期
     */
    @RequestMapping(value = "/updateDeadLine", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject updateDeadLine(@RequestParam("orderId") Long orderId, @RequestParam("deadLine") String deadLineStr) {
        //根据维修单id查询分配订单时间
        WorkOrderReportCart workOrderReportCart = workOrderFixService.updateDeadLine(orderId, deadLineStr);
        return commonDataService.getReturnType(workOrderReportCart.getDeadLine() != null, "维修单维修时限修改成功", "维修单维修时限修改失败");
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
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("param") String param,
                            @RequestParam("docName") String docName,
                            @RequestParam("titles") String titles[],
                            @RequestParam("colNames") String[] colNames,
                            @RequestParam("selectedIds") String selectedIds
    ) {
        List<VworkOrderFixBill> dataList;
        List<Long> idList = StringUtils.str2List(selectedIds, ",");
        if (idList.isEmpty()) {
            dataList = workOrderFixSearchService.findByConditions(param, 6);
        } else {
            dataList = workOrderFixSearchService.findByConditionsAndIdIn(param, idList, 6);
        }
        workOrderFixService.setDataList(dataList);
        workOrderFixService.exportExcel(request, response, docName, titles, colNames);
    }


    @ResponseBody
    @RequestMapping(value = "/findExpired", method = RequestMethod.GET)
    public Long findExpired() {

        return workOrderFixService.findExpired();
    }


    /**
     * @param orderIds
     * @return 批量完成维修工单
     */
    @ResponseBody
    @RequestMapping(value = "/finishBatch", method = RequestMethod.POST)
    public ReturnObject finishBatch(@RequestParam(value = "orderIds") String orderIds) {
        List<WorkOrderHistory> workOrderHistoryList = new ArrayList<WorkOrderHistory>();
        String[] ids = orderIds.split(",");
        for (String id : ids) {
            Long workOrderId = Long.parseLong(id);
            WorkOrderReportCart workOrderReportCart = workOrderReportCartRepository.findById(workOrderId);
            workOrderHistoryList.add(workOrderFixService.handleWorkOrder(workOrderReportCart, "批量完工", "已完工"));
        }
        return commonDataService.getReturnType(workOrderHistoryList.size() == ids.length, workOrderHistoryList.size() + "个维修单完工成功!", "维修单完工失败!");
    }

}
