package com.subway.controller.budget;


import com.subway.controller.common.BaseController;
import com.subway.domain.app.MyPage;
import com.subway.domain.budget.BudgetBill;
import com.subway.domain.budget.VbudgetBill;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.budge.BudgeSearchService;
import com.subway.service.budge.BudgeService;
import com.subway.service.commonData.CommonDataService;
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
 * 采购申请
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/budget")
class BudgetController extends BaseController {
    @Autowired
    BudgeService budgeService;
    @Autowired
    ResourceService resourceService;
    @Autowired
    CommonDataService commonDataService;


    @Autowired
    BudgeSearchService budgeSearchService;

    /**
     * 初始化分页查询采购单信息
     *
     * @param request
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
        return new PageUtils().searchBySortService(budgeSearchService, searchPhrase, 4, current, rowCount, pageable);
    }

    /**
     * @param id 根据id查询
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BudgetBill findById(@PathVariable("id") Long id) {
        return budgeService.findById(id);
    }


    /**
     * @param budgetBill 保存或者更新采购单
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject save(BudgetBill budgetBill) {
        budgetBill = budgeService.save(budgetBill);
        return commonDataService.getReturnType(budgetBill != null, "采购申请单保存成功!", "采购申请单保存失败!");

    }


    /**
     * @param id 根据id删除采购单
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean delete(@PathVariable("id") Long id) {
        return budgeService.delete(id);
    }


    /**
     * @return 查询所有的id
     */
    @RequestMapping(value = "/findAllIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> findAllIds() {
        return budgeService.findAllIds();
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
        List<VbudgetBill> dataList = budgeSearchService.findByConditions(param, 4);
        budgeService.exportExcel(request, response, docName, titles, colNames, dataList);
    }
}
