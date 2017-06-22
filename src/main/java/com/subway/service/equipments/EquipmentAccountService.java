package com.subway.service.equipments;

import com.subway.dao.equipments.EqBatchUpdateBillRepository;
import com.subway.dao.equipments.EquipmentsRepository;
import com.subway.dao.equipments.VEqRepository;
import com.subway.dao.equipments.VeqClassRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.domain.equipments.EqBatchUpdateBill;
import com.subway.domain.equipments.Equipments;
import com.subway.domain.equipments.VeqClass;
import com.subway.domain.equipments.Vequipments;
import com.subway.domain.locations.Vlocations;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.locations.LocationsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin on 2016/5/4.
 * 设备台账业务类
 */
@Service

public class EquipmentAccountService extends BaseService {

    @Autowired
    EquipmentsRepository equipmentsRepository;


    @Autowired
    VEqRepository vEqRepository;


    @Autowired
    OutsourcingUnitRepository outsourcingUnitRepository;


    @Autowired
    LocationsService locationsService;


    @Autowired
    VlocationsRepository vlocationsRepository;
    @Autowired
    VeqClassRepository veqClassRepository;
    @Autowired
    CommonDataService commonDataService;


    @Autowired
    EqBatchUpdateBillRepository eqBatchUpdateBillRepository;


    /**
     * @param equipments 保存设备信息
     * @return
     */
    public Equipments save(Equipments equipments) {
        equipments = equipmentsRepository.save(equipments);
        return equipments;
    }


    /**
     * @param eqCode 设备编号
     * @return 根据设备编号查询设备数量
     */
    public boolean checkExist(String eqCode) {

        boolean exists = equipmentsRepository.selectCountByEqcode(eqCode) > 0;
        return exists;
    }


    /**
     * @param id 根据id查询设备信息
     * @return
     */
    public Equipments findById(Long id) {
        return equipmentsRepository.findById(id);
    }


    /**
     * @return 查询所有
     */
    public List<Equipments> findAll() {
        return equipmentsRepository.findAll();
    }

    /**
     * @return 查询所有
     */
    public Page<Vequipments> findAll(Pageable pageable) {
        return vEqRepository.findAll(pageable);
    }


    /**
     * @param id 根据id删除设备信息
     */
    public void delete(Long id) {
        equipmentsRepository.delete(id);
    }


    /**
     * @param eid 保存设备信息
     * @return
     */
    public List<Object> findFixingStepByEid(Long eid) {
        return equipmentsRepository.findFixingStepByEid(eid);
    }


    public List<Object> findFixHistoryByEid(Long eid) {
        return equipmentsRepository.findFixHistoryByEid(eid);
    }

    public List<Object> findLastFixHistoryByEid(Long eid) {
        return equipmentsRepository.findLastFixHistoryByEid(eid);
    }


    public List<Object> findAllFixStepsByOrderLineNo(String orderLineNo) {
        return equipmentsRepository.findAllFixStepsByOrderLineNo(orderLineNo);
    }


    public List<Object> findAllFixStepsByEid(Long eid) {
        return equipmentsRepository.findEndFixStepsByEid(eid);
    }


    /**
     * @param eqCode
     * @return 查询维修历史信息
     */
    public Boolean eqCodeExists(String eqCode) {
        List<Equipments> equipmentsList = new ArrayList<Equipments>();
        if (eqCode != null && !eqCode.equals("")) {
            equipmentsList = equipmentsRepository.findByEqCode(eqCode);
        }
        return !equipmentsList.isEmpty();
    }


    /**
     * @param equipments
     * @return 返回true 删除成功
     */
    public Boolean delete(Equipments equipments) {
        equipmentsRepository.delete(equipments);
        Equipments e = equipmentsRepository.findById(equipments.getId());
        return e == null;
    }


    /**
     * @param equipments
     * @return 返回true 删除成功
     */
    public String abandon(Equipments equipments) {
        equipments.setStatus("2");
        equipments = equipmentsRepository.save(equipments);
        return equipments.getStatus();

    }


    public List<Long> selectAllId() {

        return equipmentsRepository.findAllId();
    }

    /**
     * @param list 批量保存
     */
    @Override
    public void save(List list) {
        equipmentsRepository.save(list);
    }


    /**
     * @param billId    申请单id
     * @param locId     位置编号
     * @param eqClassId 设备分类
     * @return
     */
    public ReturnObject batchUpdateEqs(Long billId, Long locId, Long eqClassId) {
        EqBatchUpdateBill batchUpdateBill = eqBatchUpdateBillRepository.getOne(billId);
        Vlocations vlocations = vlocationsRepository.findById(locId);
        VeqClass veqClass = veqClassRepository.findById(eqClassId);
        List<Equipments> equipmentsList = equipmentsRepository.findByLocationAndEquipmentsClassificationAndStatus(vlocations, veqClass, "1");
        int eqNum = equipmentsList.size();
        String contentDesc = vlocations.getLocName() + veqClass.getCname() + eqNum + "台设备";
//修改设备的型号；
        for (Equipments equipments : equipmentsList) {
            equipments.setEqModel(batchUpdateBill.getModel());
            equipmentsRepository.save(equipments);
            batchUpdateBill.setDataType("已更新");
        }
        return commonDataService.getReturnType(true, contentDesc + "设备更新成功!", contentDesc + "设备更新失败!");
    }

}