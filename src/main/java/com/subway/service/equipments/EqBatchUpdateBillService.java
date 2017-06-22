package com.subway.service.equipments;

import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.dao.equipments.VeqClassRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.domain.equipments.EqBatchUpdateBill;
import com.subway.service.app.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


/**
 * 设备批量更新申请单
 * by huangbin 2017年6月21日16:31:59
 */
@Service
public class EqBatchUpdateBillService extends BaseService {

    @Autowired
    EqBatchUpdateBillRepository eqBatchUpdateBillRepository;

    @Autowired
    VlocationsRepository vlocationsRepository;

    @Autowired
    VeqClassRepository veqClassRepository;


    /**
     * @param eqBatchUpdateBill 保存设备批量更新申请单信息
     * @return
     */
    public EqBatchUpdateBill save(EqBatchUpdateBill eqBatchUpdateBill) {
        eqBatchUpdateBill.setDataType("未更新");
        eqBatchUpdateBill = eqBatchUpdateBillRepository.save(eqBatchUpdateBill);
        Long locId = eqBatchUpdateBill.getLocation().getId();
        Long eqClassId = eqBatchUpdateBill.getEquipmentsClassification().getId();
        String loc = vlocationsRepository.findById(locId).getLocName();
        String eqClass = veqClassRepository.findById(eqClassId).getCname();
        String billContent = loc + eqClass + "设备更新";
        eqBatchUpdateBill.setBillContent(billContent);
        eqBatchUpdateBill = eqBatchUpdateBillRepository.save(eqBatchUpdateBill);
        return eqBatchUpdateBill;
    }


}
