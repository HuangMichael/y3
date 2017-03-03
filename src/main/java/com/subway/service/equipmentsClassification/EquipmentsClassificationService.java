package com.subway.service.equipmentsClassification;

import com.subway.dao.equipments.EquipmentsClassificationRepository;
import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.locations.Locations;
import com.subway.domain.units.Units;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin on 2016/3/24.
 */
@Service
public class EquipmentsClassificationService extends BaseService {

    @Autowired
    EquipmentsClassificationRepository equipmentsClassificationRepository;

    @Autowired
    OutsourcingUnitRepository outsourcingUnitRepository;

    @Autowired
    CommonDataService commonDataService;

    /**
     * 根据上级设备种类生成子类编号
     */
    public String getCodeByParent(EquipmentsClassification equipmentClassification) {
        String code = "";
        if (equipmentClassification != null) {
            code += equipmentClassification.getClassId();
            int childrenSize = equipmentClassification.getClassificationList().size();
            for (int i = 1; i < 3 - (childrenSize + "").length(); i++) {
                code += "0";
            }
            code += (childrenSize + 1);
        } else {
            code = "001";
        }
        return code;
    }

    /**
     * 新建设备分类
     */
    public EquipmentsClassification create(Long parentId) {
        EquipmentsClassification parent = equipmentsClassificationRepository.findById(parentId);
        EquipmentsClassification newObj = new EquipmentsClassification();
        newObj.setClassId(this.getCodeByParent(parent));
        newObj.setParent(parent);
        return newObj;

    }


    /**
     * 新建设备分类
     */
    public List<EquipmentsClassification> findAll() {
        List<EquipmentsClassification> equipmentsClassificationList = equipmentsClassificationRepository.findAll();
        return equipmentsClassificationList;
    }

    /**
     * 保存设备分类
     */
    public EquipmentsClassification save(EquipmentsClassification equipmentsClassification) {
        return equipmentsClassificationRepository.save(equipmentsClassification);
    }

    /**
     * 根据编号查询种类
     */

    public EquipmentsClassification findById(Long id) {
        return equipmentsClassificationRepository.findById(id);
    }

    /**
     * 删除设备种类
     */

    public void delete(EquipmentsClassification equipmentsClassification) {
        equipmentsClassificationRepository.delete(equipmentsClassification);
    }


    public List<Long> getUnitsByEqClassId(Long cid) {
        EquipmentsClassification equipmentsClassification;
        List<Long> idList = new ArrayList<Long>();
        if (cid != null) {
            equipmentsClassification = equipmentsClassificationRepository.findById(cid);
            List<Units> unitList = equipmentsClassification.getUnitSet();
            for (Units unit : unitList) {
                idList.add(unit.getId());
            }

            if (idList.isEmpty()) {
                idList.add(0l);
            }
        }
        return idList;
    }


    /**
     * 查询出不在当前设备分类的外委单位
     *
     * @param cid
     * @return
     */

    public List<Object> findUnitsNotInEqclass(Long cid) {
        return outsourcingUnitRepository.findUnitListByEqClassIdNotEq(cid);
    }


    /**
     * @param cid
     * @param unitsIdStr
     * @return 添加外委单位
     */
    public ReturnObject addUnits(Long cid, String unitsIdStr) {
        EquipmentsClassification equipmentsClassification = equipmentsClassificationRepository.findById(cid);
        if (unitsIdStr != null && !unitsIdStr.equals("")) {
            String[] ids = unitsIdStr.split(",");
            List<Units> unitsList = equipmentsClassification.getUnitSet();
            for (String id : ids) {
                Units units = outsourcingUnitRepository.findById(Long.parseLong(id));
                if (!unitsList.contains(units)) {
                    unitsList.add(units);
                }
            }
            equipmentsClassification.setUnitSet(unitsList);
            equipmentsClassification = equipmentsClassificationRepository.save(equipmentsClassification);
        }
        return commonDataService.getReturnType(equipmentsClassification != null, "设备分类关联外委单位成功！", "设备分类关联外委单位失败！");
    }


    /**
     * @param cid
     * @param classStr 设备分类字符串
     * @return 导入设备分类
     */

    public ReturnObject importClasses(Long cid, String classStr, String split) {
        EquipmentsClassification equipmentsClassification = equipmentsClassificationRepository.findById(cid);
        String classType = equipmentsClassification.getClassType();
        List<EquipmentsClassification> classList = null;
        EquipmentsClassification newClass;
        int records = 0;
        if (!classStr.isEmpty() && !classType.isEmpty()) {
            //根据分隔符分割
            String[] classArray = classStr.split(split);
            for (String classDesc : classArray) {
                //查询是否已经存在 类型和名称确定唯一一个设备类型
                classDesc=classDesc.trim();
                classList = equipmentsClassificationRepository.findByClassTypeAndDescription(classType, classDesc);
                if (classList.isEmpty()) {
                    newClass = new EquipmentsClassification();
                    newClass.setClassId(getClassId(equipmentsClassification));
                    newClass.setClassType(classType);
                    newClass.setDescription(classDesc);
                    newClass.setHasChild("0");
                    newClass.setLevel(equipmentsClassification.getLevel() + 1);
                    newClass.setParent(equipmentsClassification);
                    newClass.setLimitHours(72l);
                    newClass.setStatus("1");
                    equipmentsClassificationRepository.save(newClass);
                    records++;
                }
                //如果不存在，保存
            }
        }
        return commonDataService.getReturnType(records != 0, records + "条设备分类导入成功！", "设备分类导入失败！");
    }



    /**
     * @param equipmentsClassification    设备分类对象
     * @return 根据设备分类对象生成设备分类编码
     */
    public String getClassId(EquipmentsClassification  equipmentsClassification) {
        List<EquipmentsClassification> eqClassList = equipmentsClassificationRepository.findNodeByParent(equipmentsClassification);
        String classId = "";
        if (!eqClassList.isEmpty()) {
            EquipmentsClassification youngestChild = eqClassList.get(0);
            if (youngestChild != null) {
                String location = youngestChild.getClassId();
                String index = location.substring(location.length() - 2, location.length());
                if (index != null && !index.equals("")) {
                    long n = (Long.parseLong(index) + 1);
                    classId = equipmentsClassification.getClassId() + ((n < 10) ? "0" + n : n);
                }
            }
        } else {
            classId = equipmentsClassification.getClassId() + "01";
        }
        return classId;
    }

}
