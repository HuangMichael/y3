package com.subway.service.equipments;

import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.domain.equipments.EqBatchUpdateBill;
import com.subway.service.app.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


/**
 * 设备批量更新申请单
 * by huangbin 2017年6月21日16:31:59
 */
@Service
public class EqBatchUpdateBillService extends BaseService {

    EqBatchUpdateBillRepository eqBatchUpdateBillRepository;
    /**
     * @param eqBatchUpdateBill 保存设备批量更新申请单信息
     * @return
     */
    public EqBatchUpdateBill save(EqBatchUpdateBill eqBatchUpdateBill) {
        eqBatchUpdateBill = eqBatchUpdateBillRepository.save(eqBatchUpdateBill);
        return eqBatchUpdateBill;
    }




}
