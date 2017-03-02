package com.subway.controller.equipment;


import com.subway.controller.common.BaseController;
import com.subway.domain.app.MyPage;
import com.subway.domain.equipments.EqAddBill;
import com.subway.domain.equipments.VEqAddBill;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipments.EqAddBillSearchService;
import com.subway.service.equipments.EqAddBillService;
import com.subway.service.equipments.EqUpdateBillService;
import com.subway.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 设备新置
 * 2016年9月28日13:46:03
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/eqAddBill")
public class EqAddBillController extends BaseController {
    @Autowired
    EqUpdateBillService eqUpdateBillService;

    @Autowired
    EqAddBillService eqAddBillService;


    @Autowired
    EqAddBillSearchService eqAddBillSearchService;

    @Autowired
    ResourceService resourceService;
    @Autowired
    CommonDataService commonDataService;

    /**
     * 初始化分页查询设备新置申请单信息
     *
     * @param current
     * @param rowCount
     * @param searchPhrase
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "0") int current, @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount, @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {


        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(eqAddBillSearchService, searchPhrase, 5, current, rowCount,pageable);
    }


    /**
     * @param eqAddBill 保存或者更新设备更新申请单
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject save(EqAddBill eqAddBill) {
        EqAddBill budgetObj;
        String operation = "保存";
        if (eqAddBill.getId() != null) {
            operation = "更新";
        }
        budgetObj = eqAddBillService.save(eqAddBill);
        return commonDataService.getReturnType(budgetObj != null, "设备新置申请单" + operation + "成功!", "设备新置申请单" + operation + "失败!");

    }


    /**
     * @return 查询所有的id
     */
    @RequestMapping(value = "/findAllIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> findAllIds() {
        return eqAddBillService.findAllIds();
    }










    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EqAddBill findById(@PathVariable("id") Long id) {
        return eqAddBillService.findById(id);
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
        List<VEqAddBill> dataList = eqAddBillSearchService.findByConditions(param, 5);
        eqAddBillService.setDataList(dataList);
        eqAddBillService.exportExcel(request, response, docName, titles, colNames);
    }


}
