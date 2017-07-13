package com.subway.controller.equipment;


import com.subway.controller.common.BaseController;
import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.domain.app.MyPage;
import com.subway.domain.equipments.EqBatchUpdateBill;
import com.subway.domain.equipments.EqUpdateBill;
import com.subway.domain.equipments.Equipments;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.person.Person;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipments.EqBatchUpdateBillSearchService;
import com.subway.service.equipments.EqBatchUpdateBillService;
import com.subway.service.equipments.EquipmentAccountService;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.locations.LocationsService;
import com.subway.utils.CommonStatusType;
import com.subway.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 设备批量更新
 * 2017年6月20日18:18:10
 * huangbin
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/eqBatchUpdateBill")
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

    @Autowired
    EquipmentAccountService equipmentAccountService;

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
     * @param ids
     * @return 审核通过
     */
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject approve(@RequestParam("ids") String ids) {
        System.out.println("ids-----------------------" + ids);
        String idsArray[] = ids.split(",");
        for (String idStr : idsArray) {
            Long id = Long.parseLong(idStr);
            EqBatchUpdateBill eqBatchUpdateBill = eqBatchUpdateBillService.findById(id);
            eqBatchUpdateBill.setStatus("1");
            eqBatchUpdateBill = eqBatchUpdateBillService.save(eqBatchUpdateBill);
        }
        return commonDataService.getReturnType(true, "设备更新审核通过", "设备更新审核");
    }


    /**
     * @param eqUpdateBillid
     * @return 审核通过
     */
    @RequestMapping(value = "/replaceEq", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject replaceEq(@RequestParam("id") Long eqUpdateBillid) {
        return eqBatchUpdateBillService.replaceEquipment(eqUpdateBillid);
    }
}
