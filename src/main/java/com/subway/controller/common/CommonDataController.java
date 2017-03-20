package com.subway.controller.common;


import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.equipments.VeqClass;
import com.subway.domain.equipments.Vequipments;
import com.subway.domain.locations.Locations;
import com.subway.domain.locations.Vlocations;
import com.subway.domain.person.Person;
import com.subway.domain.units.Units;
import com.subway.domain.user.User;
import com.subway.object.ListObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by huangbin on 2015/12/23 0023.
 * 创建通用数据的控制器 自动选择数据加载方式
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/commonData")
public class CommonDataController extends BaseController {
    @Autowired
    CommonDataService commonDataService;

    /**
     * @param httpSession 当前会话
     * @return
     */
    @RequestMapping(value = "/findMyLocation", method = RequestMethod.GET)
    @ResponseBody
    public List<Locations> findMyLocation(HttpSession httpSession) {
        String location = SessionUtil.getCurrentUserLocationBySession(httpSession);
        List<Locations> locationList = null;
        if (location != null && !location.equals("")) {
            locationList = commonDataService.findMyLocation(location, httpSession);
        }
        return locationList;
    }


    /**
     * @param httpSession 当前会话
     * @return
     */
    @RequestMapping(value = "/findMyLoc", method = RequestMethod.GET)
    @ResponseBody
    public List<Vlocations> findMyLoc(HttpSession httpSession) {
        String location = SessionUtil.getCurrentUserLocationBySession(httpSession);
        List<Vlocations> locationList = null;
        if (location != null && !location.equals("")) {
            locationList = commonDataService.findMyVLocation(location, httpSession);
        }
        return locationList;
    }


    /**
     * @param httpSession 当前会话
     * @return
     */
    @RequestMapping(value = "/findMyEqs", method = RequestMethod.GET)
    @ResponseBody
    public List<Vequipments> findMyEqs(HttpSession httpSession) {
        String location = SessionUtil.getCurrentUserLocationBySession(httpSession);
        List<Vequipments> vequipmentsList = null;
        if (location != null && !location.equals("")) {
            vequipmentsList = commonDataService.findMyVeqs(location, httpSession);
        }
        return vequipmentsList;
    }

    /**
     * @param httpSession 当前会话
     * @return 查询分类
     */
    @RequestMapping(value = "/findEqClass", method = RequestMethod.GET)
    @ResponseBody
    public List<EquipmentsClassification> findEquipmentsClassifications(HttpSession httpSession) {
        List<EquipmentsClassification> equipmentsClassificationList = null;
        if (httpSession != null) {
            equipmentsClassificationList = commonDataService.findEquipmentsClassification(httpSession);
        }
        return equipmentsClassificationList;
    }


    /**
     * @param httpSession 当前会话
     * @return 查询分类视图信息
     */
    @RequestMapping(value = "/findVEqClass", method = RequestMethod.GET)
    @ResponseBody
    public List<VeqClass> findVeqClass(HttpSession httpSession) {
        List<VeqClass> veqClassList = null;
        if (httpSession != null) {
            veqClassList = commonDataService.findVeqClass(httpSession);
        }
        return veqClassList;
    }


    /**
     * @param type
     * @return 查询分类视图信息
     */
    @RequestMapping(value = "/findVEqClass/{type}", method = RequestMethod.GET)
    @ResponseBody
    public List<VeqClass> findVeqClassByType(@PathVariable("type") String type) {
        List<VeqClass> veqClassList = null;
            if (type == null) {
                type = "";
            }
        veqClassList = commonDataService.findVeqClassByType(type);
        return veqClassList;
    }


    /**
     * @param httpSession 当前会话
     * @return 获得设备状态
     */
    @RequestMapping(value = "/getEqStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<ListObject> getEqStatus(HttpSession httpSession) {
        return commonDataService.getEqStatus(httpSession);
    }

    /**
     * @param httpSession 当前会话
     * @return 获得设备状态
     */
    @RequestMapping(value = "/getEqRunStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<ListObject> getEqRunStatus(HttpSession httpSession) {

        return commonDataService.getRunningStatus(httpSession);
    }


    /**
     * @param httpSession 当前会话
     * @return 获得设备状态
     */
    @RequestMapping(value = "/findActivePerson", method = RequestMethod.GET)
    @ResponseBody
    public List<Person> findActivePerson(HttpSession httpSession) {
        return commonDataService.findActivePerson(httpSession);
    }


    /**
     * @param httpSession 当前会话
     * @return 查询外委单位信息
     */
    @RequestMapping(value = "/findUnits", method = RequestMethod.GET)
    @ResponseBody
    public List<Units> findUnits(HttpSession httpSession) {
        return commonDataService.findUnits(httpSession);
    }

    /**
     * @return 获得服务器时间
     */
    @RequestMapping(value = "/getServerDate", method = RequestMethod.GET)
    @ResponseBody
    public String getServerDate() {
        return commonDataService.getServerDate();
    }

    /**
     * @param session 会话
     * @return 重载session值
     */
    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reload(HttpSession session) {
        boolean result = false;
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            result = commonDataService.reload(user, session);
        }
        return result;
    }

}

