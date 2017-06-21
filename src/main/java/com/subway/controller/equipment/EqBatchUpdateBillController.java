package com.subway.controller.equipment;


import com.subway.controller.common.BaseController;
import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.domain.app.MyPage;
import com.subway.domain.equipments.EqBatchUpdateBill;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.person.Person;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipments.EqBatchUpdateBillSearchService;
import com.subway.service.equipments.EqBatchUpdateBillService;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.locations.LocationsService;
import com.subway.utils.PageUtils;
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
import java.util.Map;


/**
 * 设备批量更新
 * 2017年6月20日18:18:10
 * huangbin
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/eqBatch")
public class EqBatchUpdateBillController extends BaseController {

    @Autowired
    EqBatchUpdateBillSearchService eqBatchUpdateBillSearchService;

    @Autowired
    EqBatchUpdateBillService eqBatchUpdateBillService;

    @Autowired
    CommonDataService commonDataService;

    @Autowired
    LocationsService locationsService;

    @Autowired
    EquipmentsClassificationService equipmentsClassificationService;

    /**
     * 分页查询
     *
     * @param current      当前页
     * @param rowCount     每页条数
     * @param searchPhrase 查询关键字
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(HttpServletRequest request,
                       @RequestParam(value = "current", defaultValue = "0") int current,
                       @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount,
                       @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(eqBatchUpdateBillSearchService, searchPhrase, 2, current, rowCount, pageable);
    }


    /**
     * @param applicant
     * @return 保存人员信息
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObject save(@RequestParam("applicant") String applicant,
                             @RequestParam("loc") long loc,
                             @RequestParam("eqClassId") long eqClassId,
                             @RequestParam("applyDep") String applyDep,
                             @RequestParam("applyDate") String applyDate,
                             @RequestParam("purpose") String purpose,
                             @RequestParam("approver") String approver,
                             @RequestParam("handler") String handler,
                             @RequestParam("receiver") String receiver
    ) {
        EqBatchUpdateBill eqBatchUpdateBill = new EqBatchUpdateBill();
        eqBatchUpdateBill.setApplicant(applicant);
        eqBatchUpdateBill.setLocations(locationsService.findById(loc));
        eqBatchUpdateBill.setEquipmentsClassification(equipmentsClassificationService.findById(eqClassId));
        eqBatchUpdateBill.setApplyDep(applyDep);
        eqBatchUpdateBill.setPurpose(purpose);
        eqBatchUpdateBill.setApprover(approver);
        eqBatchUpdateBill.setHandler(handler);
        eqBatchUpdateBill.setReceiver(receiver);
        eqBatchUpdateBill.setDataType("2");
        eqBatchUpdateBill.setApplyDate(applyDate);
        eqBatchUpdateBill.setStatus("1");
        System.out.println("applicant--------" + applicant);
        System.out.println("loc--------" + loc);
        System.out.println("eqClassId--------" + eqClassId);
        System.out.println("applyDep--------" + applyDep);
        System.out.println("purpose--------" + purpose);
        System.out.println("approver--------" + approver);
        System.out.println("handler--------" + handler);
        System.out.println("receiver--------" + receiver);
        System.out.println("applyDate--------" + applyDate);
        eqBatchUpdateBill = eqBatchUpdateBillService.save(eqBatchUpdateBill);
        return commonDataService.getReturnType(eqBatchUpdateBill != null, "设备批量更新信息保存成功!", "设备批量更新信息保存失败!");
    }
}
