package com.subway.controller.equipment;


import com.subway.controller.common.BaseController;
import com.subway.domain.app.MyPage;
import com.subway.domain.equipments.*;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipments.*;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.locations.LocationsService;
import com.subway.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 设备更新
 * 2016年9月23日10:48:16
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/eqBatchUpdate")
public class EqBatchUpdateController extends BaseController {
    @Autowired
    EqUpdateBillService eqUpdateBillService;

    @Autowired
    EqUpdateBillSearchService eqUpdateBillSearchService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    CommonDataService commonDataService;

    @Autowired
    EquipmentAccountService equipmentAccountService;


    @Autowired
    EqBatchUpdateBillSearchService eqBatchUpdateBillSearchService;

    @Autowired
    EqBatchUpdateBillService eqBatchUpdateBillService;


    @Autowired
    LocationsService locationsService;

    @Autowired
    EquipmentsClassificationService equipmentsClassificationService;

    /**
     * 初始化分页查询设备更新申请单信息
     *
     * @param current
     * @param rowCount
     * @param searchPhrase
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(@RequestParam(value = "current", defaultValue = "0") int current, @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount, @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {

        return new PageUtils().searchByService(eqUpdateBillSearchService, searchPhrase, 5, current, rowCount);
    }


    /**
     * @param eid
     * @return 返回新创建的对象
     */
    @RequestMapping(value = "/create/{eid}", method = RequestMethod.GET)
    @ResponseBody
    public EqUpdateBill create(@PathVariable("eid") Long eid) {
        Equipments equipments = equipmentAccountService.findById(eid);
        EqUpdateBill eqUpdateBill = new EqUpdateBill();
        eqUpdateBill.setEquipments(equipments);
        return eqUpdateBill;
    }


    /**
     * @param id 根据id查询
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EqUpdateBill findById(@PathVariable("id") Long id) {
        return eqUpdateBillService.findById(id);
    }


//    /**
//     * @param budgetBill 保存或者更新设备更新申请单
//     * @return
//     */
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnObject save(EqUpdateBill budgetBill) {
//        EqUpdateBill budgetObj;
//        String operation = "保存";
//        if (budgetBill.getId() != null) {
//            operation = "更新";
//        }
//        budgetObj = eqUpdateBillService.save(budgetBill);
//        return commonDataService.getReturnType(budgetObj != null, "设备更新申请单" + operation + "成功!", "设备更新申请单" + operation + "失败!");
//
//    }


    /**
     * @param id 根据id删除采购单
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean delete(@PathVariable("id") Long id) {
        return eqUpdateBillService.delete(id);
    }


    /**
     * @return 查询所有的id
     */
    @RequestMapping(value = "/findAllIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> findAllIds() {
        return eqUpdateBillService.findAllIds();
    }


    /**
     * @param lid
     * @return 根据位置id过滤对应的设备分类
     */
    @RequestMapping(value = "/findCByLocId/{lid}", method = RequestMethod.GET)
    @ResponseBody
    public List<VeqClass> findEqClassesByLocationId(@PathVariable("lid") Long lid) {
        return commonDataService.findEqClassesByLocationId(lid);
    }


    /**
     * @param lid
     * @return 根据位置和设备分类过滤设备
     */
    @RequestMapping(value = "/findEqBy/{lid}/{cid}", method = RequestMethod.GET)
    @ResponseBody
    public List<Vequipments> findEqClassesByLocationId(@PathVariable("lid") Long lid, @PathVariable("cid") Long cid) {
        return commonDataService.findEqByLocIdAndEqcId(lid, cid);
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
        List<VEqUpdateBill> dataList = eqUpdateBillSearchService.findByConditions(param, 5);
        eqUpdateBillService.setDataList(dataList);
        eqUpdateBillService.exportExcel(request, response, docName, titles, colNames);
    }


//    /**
//     * @param applicant
//     * @return 保存人员信息
//     */
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnObject save(@RequestParam("applicant") String applicant,
//                             @RequestParam("loc") long loc,
//                             @RequestParam("eqClassId") long eqClassId,
//                             @RequestParam("applyDep") String applyDep,
//                             @RequestParam("applyDate") String applyDate,
//                             @RequestParam("purpose") String purpose,
//                             @RequestParam("approver") String approver,
//                             @RequestParam("handler") String handler,
//                             @RequestParam("receiver") String receiver
//    ) {
//        EqBatchUpdateBill eqBatchUpdateBill = new EqBatchUpdateBill();
//        eqBatchUpdateBill.setApplicant(applicant);
//        eqBatchUpdateBill.setLocations(locationsService.findById(loc));
//        eqBatchUpdateBill.setEquipmentsClassification(equipmentsClassificationService.findById(eqClassId));
//        eqBatchUpdateBill.setApplyDep(applyDep);
//        eqBatchUpdateBill.setPurpose(purpose);
//        eqBatchUpdateBill.setApprover(approver);
//        eqBatchUpdateBill.setHandler(handler);
//        eqBatchUpdateBill.setReceiver(receiver);
//        eqBatchUpdateBill.setDataType("2");
//        eqBatchUpdateBill.setApplyDate(applyDate);
//        eqBatchUpdateBill.setStatus("1");
//        System.out.println("applicant--------" + applicant);
//        System.out.println("loc--------" + loc);
//        System.out.println("eqClassId--------" + eqClassId);
//        System.out.println("applyDep--------" + applyDep);
//        System.out.println("purpose--------" + purpose);
//        System.out.println("approver--------" + approver);
//        System.out.println("handler--------" + handler);
//        System.out.println("receiver--------" + receiver);
//        System.out.println("applyDate--------" + applyDate);
//        eqBatchUpdateBillService.save(eqBatchUpdateBill);
//        return commonDataService.getReturnType(eqBatchUpdateBill != null, "设备批量更新信息保存成功!", "设备批量更新信息保存失败!");
//    }


    /**
     * @param eqBatchUpdateBill
     * @return 保存人员信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject save(EqBatchUpdateBill eqBatchUpdateBill) {
        eqBatchUpdateBill.setStatus("1");
        eqBatchUpdateBill = eqBatchUpdateBillService.save(eqBatchUpdateBill);
        return commonDataService.getReturnType(eqBatchUpdateBill != null, "设备批量更新信息保存成功!", "设备批量更新信息保存失败!");
    }
}
