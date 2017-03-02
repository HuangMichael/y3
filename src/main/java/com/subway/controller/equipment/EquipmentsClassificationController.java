package com.subway.controller.equipment;


import com.subway.controller.common.BaseController;
import com.subway.dao.equipments.EquipmentsClassificationRepository;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.units.Units;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.unit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin on 2015/12/23 0023.
 * 设备分类控制器类
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/equipmentsClassification")
public class EquipmentsClassificationController extends BaseController {

    @Autowired
    EquipmentsClassificationRepository equipmentsClassificationRepository;
    @Autowired
    EquipmentsClassificationService equipmentsClassificationService;
    @Qualifier("unitService")
    UnitService unitService;
    @Autowired
    ResourceService resourceService;


    /**
     * @param id id
     * @return 根据ID查询节点对象
     */
    @RequestMapping(value = "/findById/{id}")
    @ResponseBody
    public EquipmentsClassification findById(@PathVariable("id") Long id) {
        EquipmentsClassification eqClass = equipmentsClassificationRepository.findById(id);
        return eqClass;
    }


    /**
     * 查询根节点
     */
    @RequestMapping(value = "/findNodeByParentId")
    @ResponseBody
    public List<EquipmentsClassification> findNodeByParentId() {
        List<EquipmentsClassification> equipmentsClassificationList = equipmentsClassificationRepository.findNodeByParentId();
        return equipmentsClassificationList;
    }

    /**
     * 查询根节点
     */
    @RequestMapping(value = "/findNodeByParentId/{id}")
    @ResponseBody
    public List<EquipmentsClassification> findNodeByParentId(@PathVariable("id") long id) {
        EquipmentsClassification equipmentClassification = equipmentsClassificationRepository.findById(id);
        List<EquipmentsClassification> equipmentsClassificationList = equipmentsClassificationRepository.findNodeByParent(equipmentClassification);
        return equipmentsClassificationList;
    }

    /**
     * 显示设备分类明细
     *
     * @param id       设备分类id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap modelMap) {
        String url = "/equipmentsClassification";
        EquipmentsClassification object = null;
        List<Units> unitList = new ArrayList<Units>();
        if (id != 0) {
            url += "/detail";
            object = equipmentsClassificationRepository.findById(id);
            unitList = object.getUnitSet();
        }
        modelMap.put("equipmentsClassification", object);
        modelMap.put("unitList", unitList);
        //查询出当前设备分类的所有维修单位
        return url;
    }

    /**
     * 查询根节点
     */
    @RequestMapping(value = "/create/{id}")
    public String create(@PathVariable("id") Long id, ModelMap modelMap) {
        EquipmentsClassification newObj = equipmentsClassificationService.create(id);
        modelMap.put("equipmentsClassification", newObj);
        List<EquipmentsClassification> equipmentsClassificationList = equipmentsClassificationService.findAll();
        modelMap.put("equipmentsClassificationList", equipmentsClassificationList);
        return "/equipmentsClassification/create";
    }


    /**
     * 查询根节点
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public EquipmentsClassification save(@RequestParam("lid") Long lid,
                                         @RequestParam("description") String description,
                                         @RequestParam("parentId") Long parentId,
                                         @RequestParam("classId") String classId,
                                         @RequestParam(value = "limitHours", required = false) Long limitHours) {
        EquipmentsClassification newObj;
        EquipmentsClassification parent = equipmentsClassificationService.findById(parentId);
        if (lid == null || lid == 0) {
            newObj = new EquipmentsClassification();
            newObj.setClassId(classId);
            newObj.setDescription(description);
            newObj.setLimitHours(limitHours);
            newObj.setLevel(parent.getLevel() + 1);
            newObj.setStatus("1");
            newObj.setClassType(parent.getClassType());
            newObj.setParent(parent);
        } else {
            newObj = equipmentsClassificationService.findById(lid);
            newObj.setDescription(description);
            newObj.setParent(parent);
            newObj.setLimitHours(limitHours);
            newObj.setClassType(parent.getClassType());
            newObj.setLevel(parent.getLevel() + 1);
            newObj.setStatus("1");
        }
        newObj = equipmentsClassificationService.save(newObj);
        return newObj;
    }


    /**
     * 查询所有设备分类
     *
     * @return
     */
    @RequestMapping(value = "/findAll")
    @ResponseBody
    public List<EquipmentsClassification> findAll() {
        List<EquipmentsClassification> equipmentClassificationList = equipmentsClassificationRepository.findAll();
        return equipmentClassificationList;
    }


    /**
     * 删除设备分类信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean delete(@PathVariable("id") Long id) {
        EquipmentsClassification equipmentsClassification = equipmentsClassificationService.findById(id);
        if (equipmentsClassification != null) {
            equipmentsClassificationService.delete(equipmentsClassificationService.findById(id));
        }
        return equipmentsClassificationService.findById(id) == null;
    }

   /* *//**
     * 根据设备分类查询不是该分类对应的外委单位信息
     *
     * @param cid 设备分类编号
     * @return 返回 id  外委单位名称
     *//*
    @RequestMapping(value = "/findUnitByClassIdNotEq/{cid}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> findUnitByClassIdNotEq(@PathVariable("cid") Long cid) {
        return unitService.findUnitListByEqClassIdNotEq(cid);
    }*/


    /**
     * 根据设备分类查询对应的外委单位信息
     *
     * @param cid 设备分类编号
     * @return 返回 id  外委单位名称
     */
    @RequestMapping(value = "/findUnitListByEqClassId/{cid}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> findUnitListByEqClassId(@PathVariable("cid") Long cid) {
        return unitService.findUnitListByEqClassIdEq(cid);
    }

    /**
     * @param cid 设备分类id
     * @param ids 选中的外委单位id
     * @return
     */
    @RequestMapping(value = "/addUnits", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject addUnits(@RequestParam("cid") Long cid, @RequestParam("ids") String ids) {
        return equipmentsClassificationService.addUnits(cid, ids);
    }


    /**
     * @param cid         设备分类id
     * @param ids         外委单位id
     * @param workOrderId
     * @return 将外委单位加入设备分类
     */
    @RequestMapping(value = "/addU2c", method = RequestMethod.POST)
    @ResponseBody
    public List<Units> addU2c(@RequestParam("cid") Long cid, @RequestParam("ids") String ids, @RequestParam("workOrderId") Long workOrderId) {
        List<Units> outsourcingUnitList = new ArrayList<Units>();
        if (cid != null && ids != null) {
            outsourcingUnitList = unitService.addU2c(cid, ids, workOrderId);
        }
        return outsourcingUnitList;
    }

    @RequestMapping(value = "/removeUnits", method = RequestMethod.POST)
    @ResponseBody
    public EquipmentsClassification removeUnits(@RequestParam("cid") Long cid, @RequestParam("ids") String ids) {
        EquipmentsClassification equipmentsClassification = null;
        if (cid != null && ids != null) {
            equipmentsClassification = unitService.removeUnits(cid, ids);
        }
        return equipmentsClassification;
    }


    /**
     * @param cid 设备分类ID
     * @return 根据设备分类的ID查询对应的外委单位的ID集合
     */
    @RequestMapping(value = "/getUnitsByEqClassId/{cid}", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getUnitsByEqClassId(@PathVariable("cid") Long cid) {
        List<Long> longList = equipmentsClassificationService.getUnitsByEqClassId(cid);
        return longList;
    }


    /**
     * 载入明细信息
     */
    @RequestMapping(value = "/popUnits/{cid}", method = RequestMethod.GET)
    public String popUsers(@PathVariable("cid") Long cid, ModelMap modelMap) {
        List<Object> unitsNotInEqClass = equipmentsClassificationService.findUnitsNotInEqclass(cid);
        modelMap.put("unitsNotInEqClass", unitsNotInEqClass);
        return "/equipmentsClassification/popUnits";
    }


    /**
     * @param cid      设备分类ID
     * @param classStr 分类字符串
     * @param split    分隔符
     * @return 返回导入成功失败标识
     */
    @RequestMapping(value = "/importClass", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject importClass(@RequestParam("cid") Long cid, @RequestParam("classStr") String classStr, @RequestParam("split") String split) {
        return equipmentsClassificationService.importClasses(cid, classStr, split);
    }
}
