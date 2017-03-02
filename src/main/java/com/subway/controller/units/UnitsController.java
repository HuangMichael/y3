package com.subway.controller.units;


import com.subway.controller.common.BaseController;
import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.domain.app.MyPage;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.units.Units;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.unit.UnitSearchService;
import com.subway.service.unit.UnitService;
import com.subway.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbin on 2016/03/23 0023.
 * 外委单位控制器类
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/units")
public class UnitsController extends BaseController {

    @Autowired
    OutsourcingUnitRepository outsourcingUnitRepository;
    @Autowired
    UnitService unitService;
    @Autowired
    CommonDataService commonDataService;

    @Autowired
    UnitSearchService unitSearchService;


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
    public MyPage data(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "0") int current, @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount, @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(unitSearchService, searchPhrase, 2, current, rowCount, pageable);
    }


    /**
     * @return 查询 所有的外委单位信息
     */
    @RequestMapping(value = "/findAll")
    @ResponseBody
    public List<Units> findAll() {
        List<Units> outsourcingUnitList = unitService.findAll();
        return outsourcingUnitList;
    }


    /**
     * @param unitId   外委单位信息
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/detail/{unitId}", method = RequestMethod.GET)
    public String detail(@PathVariable("unitId") Long unitId, ModelMap modelMap) {
        Units unit = outsourcingUnitRepository.findById(unitId);
        if (unit != null) {
            modelMap.put("unit", unit);
        }
        return "/units/detail";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject save(Units units) {
        units.setStatus("1");
        units = outsourcingUnitRepository.save(units);
        return commonDataService.getReturnType(units != null, "单位信息保存成功", "单位信息保存失败");
    }


    @RequestMapping(value = "/saveLink", method = RequestMethod.POST)
    @ResponseBody
    public List<Units> saveLink(
            @RequestParam("description") String description,
            @RequestParam("linkman") String linkman,
            @RequestParam("telephone") String telephone,
            @RequestParam("eqClassId") Long eqClassId,
            @RequestParam("workDays") String workDays
    ) {
        Units units = new Units();
        units.setDescription(description);
        units.setLinkman(linkman);
        units.setTelephone(telephone);
        units.setWorkDays(workDays);
        units.setStatus("1");
        units = outsourcingUnitRepository.save(units);
        return unitService.saveLink(units, eqClassId);
    }


    /**
     * @param id 根据id删除设备信息
     */
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public Boolean delete(@PathVariable("id") Long id) {
        Units unit = null;
        if (id != null) {
            unit = unitService.findById(id);
        }
        return unitService.delete(unit);

    }


    @RequestMapping(value = "/downloadContract/{contractId}", method = RequestMethod.GET)
    public String downloadContract(@PathVariable("contractId") Long contractId, ModelMap modelMap) {
        return "/docs/contracts/abc.doc";
    }


    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Units findById(@PathVariable("id") Long id) {
        return outsourcingUnitRepository.findById(id);
    }

    /**
     * @param status 状态
     * @return 根据状态查询外委单位信息
     */
    @RequestMapping(value = "/findByStatus/{status}", method = RequestMethod.GET)
    @ResponseBody
    public List<Units> findByStatus(@PathVariable("status") String status) {
        return outsourcingUnitRepository.findByStatus(status);
    }


    /**
     * @param status 状态
     * @return 根据状态查询外委单位信息
     */
    @RequestMapping(value = "/findOutUnits/{status}", method = RequestMethod.GET)
    @ResponseBody
    public List<Units> findByStatusAndOutFlag(@PathVariable("status") String status) {
        return outsourcingUnitRepository.findByStatusAndOutFlag(status, "1");
    }


    @RequestMapping(value = "/loadPageByUrl/{pageUrl}/{uid}", method = RequestMethod.GET)
    public String loadPageByUrl(@PathVariable("pageUrl") String pageUrl, @PathVariable("uid") Long uid, ModelMap modelMap) {
        Units unit = outsourcingUnitRepository.findById(uid);
        modelMap.put("unit", unit);
        return "/units/" + pageUrl;
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
        List<Units> dataList = unitSearchService.findByConditions(param, 2);
        unitService.setDataList(dataList);
        unitService.exportExcel(request, response, docName, titles, colNames);
    }


    /**
     * @return 查询所有的id
     */
    @RequestMapping(value = "/findAllIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> findAllIds() {
        return unitService.findAllIds();
    }


    /**
     * @return 根据外委单位查询其对应的设备分类
     * @author huangbin
     * 2016年12月16日09:01:03
     */
    @RequestMapping(value = "/findEqsByUnit/{unitId}", method = RequestMethod.GET)
    @ResponseBody
    public List<EquipmentsClassification> findEqClassesByUnit(@PathVariable Long unitId) {
        List<EquipmentsClassification> equipmentsClassificationList = new ArrayList<EquipmentsClassification>();
        Units units = unitService.findById(unitId);
        if (units != null) {
            equipmentsClassificationList = units.getClassificationSet();
        }
        return equipmentsClassificationList;
    }
}
