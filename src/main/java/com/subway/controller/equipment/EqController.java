package com.subway.controller.equipment;


import com.subway.dao.equipments.EqRepository;
import com.subway.dao.equipments.VEqRepository;
import com.subway.domain.app.resoure.VRoleAuthView;
import com.subway.domain.equipments.Vequipments;
import com.subway.service.app.ResourceService;
import com.subway.service.equipments.EquipmentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 可分页控制器
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/eq")
public class EqController {

    @Autowired
    EquipmentAccountService equipmentAccountService;
    @Autowired
    EqRepository repository;

    @Autowired
    VEqRepository vEqRepository;
    @Autowired
    ResourceService resourceService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpSession httpSession, ModelMap modelMap) {
        List<VRoleAuthView> appMenus = resourceService.findAppMenusByController(httpSession, "equipment");
        modelMap.put("appMenus", appMenus);
        return "/eq/list";

    }

    @RequestMapping(value = "/listVeq/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Page<Vequipments> listVeq(@PathVariable(value = "pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
        Page<Vequipments> equipmentsPage = vEqRepository.findAll(new PageRequest(pageIndex, pageSize));
        return equipmentsPage;

    }


    @RequestMapping(value = "/loadPage/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public String loadPage(@PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize, ModelMap modelMap) {
        Page<Vequipments> equipmentsPage = vEqRepository.findAll(new PageRequest(pageIndex, pageSize));
        List<Vequipments> vequipmentsList = equipmentsPage.getContent();
        modelMap.put("vequipmentsList", vequipmentsList);
        return "/eq/eqList";

    }


    /**
     * @param eqName
     * @param locName
     * @param eqClass
     * @param pageIndex
     * @param pageCount
     * @return 模糊查询设备分页信息
     */
    @RequestMapping(value = "/queryByLike", method = RequestMethod.POST)
    public String queryByLike(
            @RequestParam(value = "eqName", required = false) String eqName,
            @RequestParam(value = "locName", required = false) String locName,
            @RequestParam(value = "eqClass", required = false) String eqClass,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            ModelMap modelMap) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageCount == null) {
            pageCount = 10;
        }
        Pageable pageable = new PageRequest(pageIndex, pageCount);
        Page<Vequipments> vequipmentsPage = vEqRepository.findByEqNameContainsAndLocNameContainsAndEqClassContains(eqName, locName, eqClass, pageable);
        modelMap.put("vequipmentsList", vequipmentsPage.getContent());
        return "/eq/eqList";
    }

    /**
     * @param id 设备ID编号
     * @return 返回设备信息
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Vequipments findById(@PathVariable Long id) {
        Vequipments vequipments = vEqRepository.findOne(id);
        return vequipments;
    }
}
