package com.subway.controller.portal;


import com.subway.domain.workOrder.VworkOrderLineCompare;
import com.subway.domain.workOrder.VworkOrderStationCompareVo;
import com.subway.service.portal.PortalService;
import com.subway.service.workOrder.WorkOrderReportCartService;
import com.subway.domain.workOrder.VlineMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by huangbin on 2015/12/23 0023.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/portal")
@SessionAttributes("menuList")
public class PortalController {


    @Autowired
    WorkOrderReportCartService workOrderReportCartService;


    @Autowired
    PortalService portalService;


    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap) {

        return "/portal/index";
    }

    @RequestMapping(value = "/list")
    public String list(ModelMap modelMap) {

        return "/portal/list";
    }


    /**
     * @param reportMonth 月份
     * @return
     */
    @RequestMapping(value = "/findTopEqClass/{reportMonth}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> findTopNReportCartByEqClass(@PathVariable(value = "reportMonth") String reportMonth) {
        return portalService.findTopNReportByEqClass(reportMonth);
    }


    @RequestMapping(value = "/getLastNMonth/{n}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getLastNMonth(@PathVariable("n") int n) {
        return workOrderReportCartService.getLastNMonthName(n);
    }


    /**
     * @param reportMonth 月份
     * @param name        工单状态
     * @return 按照线别查询报修的数据
     */
    @RequestMapping(value = "/getLineReportNum/{reportMonth}/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<VlineMonth> getLineReportNumReportMonth(@PathVariable("reportMonth") String reportMonth, @PathVariable("name") String name) {
        return portalService.getLineReportNumReportMonth(reportMonth, name);
    }


    /**
     * @param reportMonth 报修月份
     * @param status      工单状态
     * @return
     */

    @RequestMapping(value = "/findLineStatusStat", method = RequestMethod.POST)
    @ResponseBody
    public List<VworkOrderLineCompare> findByReportMonthAndStatusOrderByLineNoAsc(@RequestParam("reportMonth") String reportMonth, @RequestParam("status") String status) {
        return portalService.findByReportMonthAndStatusOrderByNameAsc(reportMonth, status);
    }


    /**
     * @param reportMonth 报修月份
     * @param status      工单状态
     * @return
     */

    @RequestMapping(value = "/findStationStatusStat", method = RequestMethod.POST)
    @ResponseBody
    public List<VworkOrderStationCompareVo> findByReportMonthAndStatusOrderByStationNoAsc(@RequestParam("reportMonth") String reportMonth, @RequestParam("status") String status) {
        return portalService.findByReportMonthAndStatusOrderByStationNoAsc(reportMonth, status);
    }

}

