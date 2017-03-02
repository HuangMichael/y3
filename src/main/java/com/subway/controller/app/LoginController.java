package com.subway.controller.app;

import com.subway.dao.equipments.VeqClassRepository;
import com.subway.domain.app.org.SystemInfo;
import com.subway.domain.equipments.VeqClass;
import com.subway.domain.locations.Vlocations;
import com.subway.domain.user.User;
import com.subway.object.ReturnObject;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.equipmentsClassification.EquipmentsClassificationService;
import com.subway.service.line.LineService;
import com.subway.service.line.StationService;
import com.subway.service.locations.LocationsService;
import com.subway.service.org.SysInfoService;
import com.subway.service.user.UserService;
import com.subway.utils.MD5Util;
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
import java.util.List;

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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session, ModelMap modelMap) {


        if (userName == null || userName.equals("")) {
            modelMap.put("error", "用户名不能为空！");
        }
        if (password == null || password.equals("")) {
            modelMap.put("error", "密码不能为空！");
        }
        String encryptPassword = MD5Util.md5(password);
        List<User> userList = userService.findByUserNameAndPasswordAndStatus(userName, encryptPassword, "1");
        if (!userList.isEmpty()) {
            User currentUser = userList.get(0);
            if (currentUser != null) {
                commonDataService.reload(currentUser, session);
            }
            return "redirect:/portal/index";
        } else {
            return "/index";
        }
    }


    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject checkLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) {
        String encryptPassword = MD5Util.md5(password);
        ReturnObject returnObject = new ReturnObject();
        List<User> userList = userService.findByUserNameAndPasswordAndStatus(userName, encryptPassword, "1");
        if (userList.size() == 1) {
            returnObject.setResult(true);
            returnObject.setResultDesc("用户登录成功");
            User currentUser = userList.get(0);
            List<Vlocations> locationsList = locationsService.findByLocationStartingWithAndStatus(currentUser.getVlocations().getLocation());
            List<VeqClass> veqClassList = commonDataService.findVeqClass(session);
            session.setAttribute("currentUser", currentUser);
            session.setAttribute("personName", currentUser.getPerson().getPersonName());
            SystemInfo systemInfo = sysInfoService.findBySysName("system_name");
            session.setAttribute("org", systemInfo);
            session.setAttribute("locationsList", locationsList);
            session.setAttribute("veqClassList", veqClassList);

        } else {
            returnObject.setResult(false);
            returnObject.setResultDesc("用户登录失败");
        }
        return returnObject;
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
