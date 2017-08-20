package com.subway.controller.app;


import com.subway.controller.common.BaseController;
import com.subway.dao.app.resource.VRoleAuthViewRepository;
import com.subway.domain.app.MyPage;
import com.subway.domain.app.resoure.Resource;
import com.subway.domain.app.resoure.VRoleAuthView;
import com.subway.domain.role.Role;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.commonData.CommonDataService;
import com.subway.service.role.RoleService;
import com.subway.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by huangbin on 2017-08-19 19:56:20
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/authorityData")
public class AuthorityDataController extends BaseController {
    @Autowired
    ResourceService resourceService;

    @Autowired
    RoleService roleService;
    @Autowired
    VRoleAuthViewRepository vRoleAuthViewRepository;

    @Autowired
    UserService userService;


    @Autowired
    CommonDataService commonDataService;

    /**
     * 初始化展示授权列表
     *
     * @param locationId 位置信息id
     * @param userIds    选择的用户ids
     * @return
     */
    @RequestMapping(value = "/grant", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject grant(@RequestParam("locationId") Long locationId, @RequestParam("userIds") String userIds) {


        userService.grantDataAuth(locationId, userIds);


        return commonDataService.getReturnType(true, "数据授权成功!", "数据授权失败!");
    }

    /**
     * @param roleId
     * @param modelMap
     * @return 根据角色ID载入用户权限预览
     */
    @RequestMapping(value = "/loadAuthView/{roleId}", method = RequestMethod.GET)
    public String loadAuthView(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        Role role = roleService.findById(roleId);
        List<VRoleAuthView> vRoleAuthViews = vRoleAuthViewRepository.findByRole(role);
        modelMap.put("vRoleAuthViews", vRoleAuthViews);
        return "resource/authList";
    }


    /**
     * @param roleId
     * @param modelMap
     * @return 根据角色ID载入用户权限预览
     */
    @RequestMapping(value = "/loadByRole/{roleId}", method = RequestMethod.GET)
    public String loadByRole(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        Role role = roleService.findById(roleId);
        List<VRoleAuthView> vRoleAuthViews = vRoleAuthViewRepository.findByRole(role);
        modelMap.put("vRoleAuthViews", vRoleAuthViews);
        return "resource/authList";
    }


    @RequestMapping(value = "/loadByRole/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public MyPage loadByRole(@RequestParam(value = "current", defaultValue = "0") int current,
                             @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount,
                             @RequestParam(value = "searchPhrase", required = false) String searchPhrase,
                             @PathVariable(value = "roleId") Long roleId) {

        Role role = roleService.findById(roleId);
        Page<VRoleAuthView> page = vRoleAuthViewRepository.findByRoleAndResourceNameContaining(role, searchPhrase, new PageRequest(current - 1, rowCount.intValue()));
        MyPage myPage = new MyPage();
        myPage.setRows(page.getContent());
        myPage.setRowCount(rowCount);
        myPage.setCurrent(current);
        myPage.setTotal(page.getTotalElements());
        return myPage;
    }


    /**
     * @param roleId
     * @param modelMap
     * @return 根据角色ID载入用户权限预览
     */
    @RequestMapping(value = "/loadAuth/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public List<VRoleAuthView> loadAuth(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        Role role = roleService.findById(roleId);
        List<VRoleAuthView> authViewList = vRoleAuthViewRepository.findByRole(role);
        return authViewList;
    }


    /**
     * @param userId
     * @return 根据用户ID载入用户权限预览
     */
    @RequestMapping(value = "/loadModule/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public List<VRoleAuthView> loadAuthViewByUserId(@PathVariable("userId") Long userId) {
        List<VRoleAuthView> vRoleAuthViews = resourceService.findResourcesByUserIdAndResourceLevel(userId, 1l);
        return vRoleAuthViews;
    }


    /**
     * @param userId
     * @return 根据用户ID载入用户权限预览
     */


    @RequestMapping(value = "/loadApp/{moduleId}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public List<VRoleAuthView> loadAuthViewByUserIdAndModuleId(@PathVariable("moduleId") Long moduleId, @PathVariable("userId") Long userId) {
        List<VRoleAuthView> vRoleAuthViews = resourceService.findResourcesByUserIdAndResourceLevelAndParentId(userId, 2l, moduleId);
        return vRoleAuthViews;
    }


    /**
     * @param request  请求
     * @param response 响应
     * @param param    查询关键字
     * @param docName  文档名称
     * @param titles   标题集合
     * @param colNames 列名称
     */
    @ResponseBody
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam("param") String param, @RequestParam("docName") String docName, @RequestParam("titles") String titles[], @RequestParam("colNames") String[] colNames) {
        List<VRoleAuthView> dataList = resourceService.findByRoleAndResourceNameContaining(1l, param);
        resourceService.setDataList(dataList);
        resourceService.exportExcel(request, response, docName, titles, colNames);
    }

    /**
     * @param roleId     角色id
     * @param resourceId 资源id
     */
    @ResponseBody
    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public void revoke(@RequestParam("roleId") Long roleId, @RequestParam("resourceId") Long resourceId) {
        resourceService.deleteRoleAuth(roleId, resourceId);
    }


    /**
     * 载入明细信息
     *
     * @param locationId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/popUsers/{locationId}", method = RequestMethod.GET)
    public String popUsers(@PathVariable("locationId") Long locationId, ModelMap modelMap) {
        List<Object> usersNotInLocation = userService.findUsersNotInLocation(locationId);
        modelMap.put("usersNotInLocation", usersNotInLocation);
        return "/authorityData/popUsers";
    }


    /**
     * 载入该位置下数据授权的用户
     *
     * @param locationId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/loadUsers/{locationId}", method = RequestMethod.GET)
    public String loadUsers(@PathVariable("locationId") Long locationId, ModelMap modelMap) {
        List<Object> usersInLocation = userService.findUsersInLocation(locationId);
        modelMap.put("usersInLocation", usersInLocation);
        return "/authorityData/locUsers";
    }

    /**
     * @param locationId 位置id
     * @param userIds    用户id字符串
     * @return 将locationId的数据权限授予users
     */
    @RequestMapping(value = "/grantDataAuth", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject grantDataAuth(@RequestParam("locationId") Long locationId, @RequestParam("userIds") String userIds) {
        return userService.grantDataAuth(locationId, userIds);
    }
}
