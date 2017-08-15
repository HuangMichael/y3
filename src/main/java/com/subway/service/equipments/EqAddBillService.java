package com.subway.service.equipments;

import com.subway.dao.equipments.EqAddBillRepository;
import com.subway.dao.equipments.VEqAddBillRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.domain.equipments.EqAddBill;
import com.subway.domain.equipments.Equipments;
import com.subway.domain.equipments.VEqAddBill;
import com.subway.domain.locations.Vlocations;
import com.subway.service.app.BaseService;
import com.subway.utils.CommonStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备新置申请单业务类
 */
@Service
public class EqAddBillService extends BaseService {


    @Autowired
    EqAddBillRepository eqAddBillRepository;

    @Autowired
    VEqAddBillRepository vEqAddBillRepository;

    @Autowired
    EquipmentAccountService equipmentAccountService;

    @Autowired
    VlocationsRepository vlocationsRepository;


    /**
     * @param eqName
     * @param pageable 分页
     * @return 按照配件名称模糊查询分页查询
     */
    public Page<VEqAddBill> findByEqNameContaining(String eqName, Pageable pageable) {
        return vEqAddBillRepository.findByEqNameContaining(eqName, pageable);
    }


    /**
     * @param eqName
     * @return 按照配件名称模糊查询分页查询
     */
    public List<VEqAddBill> findByEqNameContaining(String eqName) {
        return vEqAddBillRepository.findByEqNameContaining(eqName);
    }


    /**
     * @return 查询所有
     */
    public EqAddBill findById(Long id) {
        return eqAddBillRepository.findById(id);
    }


    /**
     * @return 保存或者更新设备新置申请单
     */
    @Transactional
    public EqAddBill save(EqAddBill budgetBill) {
        budgetBill.setDataType("1"); //1为新置 2为更新
        EqAddBill result = eqAddBillRepository.save(budgetBill);
        //如果是更新 不再添加设备台账
        if (budgetBill.getEquipment() == null) {
            Equipments equipments = addEq(budgetBill);

            //保存完再更新设备的id到设备履历
            budgetBill.setEquipment(equipments);
            eqAddBillRepository.save(budgetBill);
        }
        return result;
    }


    /**
     * @param id 根据id删除 删除成功返回true
     * @return
     */
    public boolean delete(Long id) {
        if (id != null) {
            eqAddBillRepository.delete(id);
        }
        return eqAddBillRepository.findById(id) == null;
    }


    /**
     * @return 查询所有的id
     */
    public List<Long> findAllIds() {
        List<Long> ids = eqAddBillRepository.findAllIds();
        return ids;
    }


    /**
     * @param eid 设备id
     * @return 根据设备id查询设备的更新历史
     */
    /*public List<EqAddBill> getUpdateHistoryById(Long eid) {
        return eqAddBillRepository.findByEquipmentsId(eid);
    }*/

    /**
     * @param eqAddBill 设备新置申请单
     * @return 根据设备新置申请单增加设备台账
     */
    @Transactional
    public Equipments addEq(EqAddBill eqAddBill) {
        Vlocations vlocations = vlocationsRepository.findById(eqAddBill.getLocation().getId());
        Equipments equipments = new Equipments();
        equipments.setEqCode(eqAddBill.getEqCode());
        equipments.setEquipmentsClassification(eqAddBill.getEqClass());
//        equipments.setLocations(eqAddBill.getLocation());
        equipments.setLocation(vlocations.getLocation());
        equipments.setDescription(eqAddBill.getEqName());
        equipments.setVlocations(vlocations);
        equipments.setRunning(CommonStatusType.STATUS_ON);
        equipments.setStatus(CommonStatusType.STATUS_YES);
        equipments = equipmentAccountService.save(equipments);

        System.out.print(equipments.toString());

        return equipments;
    }

}
