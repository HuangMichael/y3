package com.subway.service.equipments;

import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.dao.equipments.EquipmentsClassificationRepository;
import com.subway.dao.equipments.VeqClassRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.domain.equipments.*;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    CommonDataService commonDataService;


    @Autowired
    EquipmentAccountService equipmentAccountService;


    /**
     * @param eqBatchUpdateBill 保存设备批量更新申请单信息
     * @return
     */
    public EqBatchUpdateBill save(EqBatchUpdateBill eqBatchUpdateBill) {
        eqBatchUpdateBill.setDataType("未更新");
        eqBatchUpdateBill = eqBatchUpdateBillRepository.save(eqBatchUpdateBill);
        String billContent = "设备更新";
        eqBatchUpdateBill.setBillContent(billContent);
        List<EqBatchUpdateBillDetail> eqBatchUpdateBillDetailList = addEqList(eqBatchUpdateBill);
        eqBatchUpdateBill.setBillDetailList(eqBatchUpdateBillDetailList);
        Equipments equipments = eqBatchUpdateBillDetailList.get(0).getEquipments();
        VeqClass veqClass = veqClassRepository.findById(equipments.getEquipmentsClassification().getId());
        eqBatchUpdateBill.setEqClass(veqClass);
        eqBatchUpdateBill.setLocations(equipments.getVlocations());
        eqBatchUpdateBill = eqBatchUpdateBillRepository.save(eqBatchUpdateBill);
        return eqBatchUpdateBill;
    }


    /**
     * @param eqBatchUpdateBill
     * @return
     */
    public List<EqBatchUpdateBillDetail> addEqList(EqBatchUpdateBill eqBatchUpdateBill) {
        EqBatchUpdateBillDetail eqBatchUpdateBillDetail;
        List<EqBatchUpdateBillDetail> eqBatchUpdateBillDetailList = new ArrayList<EqBatchUpdateBillDetail>();
        for (String eId : eqBatchUpdateBill.getEqIds().split(",")) {
            Equipments equipments = equipmentAccountService.findById(Long.parseLong(eId));
            eqBatchUpdateBillDetail = new EqBatchUpdateBillDetail();
            eqBatchUpdateBillDetail.setEquipments(equipments);
            eqBatchUpdateBillDetail.setBill(eqBatchUpdateBill);
            eqBatchUpdateBillDetail.setStatus("0");
            String memo = eqBatchUpdateBill.getApplyDep() + eqBatchUpdateBill.getApplyDate() + eqBatchUpdateBill.getApplicant() + "申请" + eqBatchUpdateBill.getBillContent();
            eqBatchUpdateBillDetail.setMemo(memo);
            eqBatchUpdateBillDetailList.add(eqBatchUpdateBillDetail); 
        }
        return eqBatchUpdateBillDetailList;
    }

}
