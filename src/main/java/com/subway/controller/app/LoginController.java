package com.subway.controller.app;

import com.subway.dao.equipments.VeqClassRepository;
import com.subway.domain.app.org.SystemInfo;
import com.subway.domain.user.User;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.line.LineService;
import com.subway.service.line.StationService;
import com.subway.service.locations.LocationsService;
import com.subway.service.org.SysInfoService;
import com.subway.service.user.UserService;
import com.subway.utils.ConstantUtils;
import com.subway.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by huangbin on 2016/4/22.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    SysInfoService sysInfoService;

    @Autowired
    LocationsService locationsService;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Autowired
    EquipmentsClassificationService equipmentsClassificationService;

    @Autowired
    VeqClassRepository veqClassRepository;

    @Autowired
    CommonDataService commonDataService;


    @RequestMapping("/")
    public String logout(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        SystemInfo systemInfo = sysInfoService.findBySysName("system_name");
        if (session.getId() != null) {
            request.removeAttribute("currentUser");
            request.removeAttribute("locationsList");
            request.removeAttribute("equipmentsClassificationList");
            request.getSession().invalidate();
        }
        modelMap.put("sysName", systemInfo.getDescription());
        return "/index";
    }
    /**
     * @param request
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject login(HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("password") String password) {
        //根据用户名和密码查询用户信息
        //如果返回用户信息User
        try {
            password = Md5Utils.md5(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userService.findByUserNameAndPasswordAndStatus(request, userName, password, ConstantUtils.STATUS_YES);
    }

    /**
     * @return 获取当前用户信息
     */
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public User checkSession(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user;
    }
}
