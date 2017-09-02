package com.subway.controller.common;


import com.subway.domain.app.resoure.VRoleAuthView;
import com.subway.object.ReturnObject;
import com.subway.service.app.ResourceService;
import com.subway.service.commonData.CommonDataService;
import com.subway.utils.SessionUtil;
import com.subway.utils.StringUtils;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 基础控制器
 */
@Controller
@Data
@EnableAutoConfiguration
public class BaseController {
    protected Log log = LogFactory.getLog(this.getClass());
    @Autowired
    ResourceService resourceService;

    @Autowired
    CommonDataService commonDataService;
    String userLocation;


    @RequestMapping(value = "/list")
    public String list(HttpSession httpSession, ModelMap modelMap) {
        //加载查询菜单
        String controllerName = this.getClass().getSimpleName().split("Controller")[0];
        List<VRoleAuthView> appMenus = resourceService.findAppMenusByController(httpSession, controllerName.toUpperCase());
        modelMap.put("appMenus", appMenus);
        String url = "/" + StringUtils.lowerCaseCamel(controllerName) + "/list";
        return url;
    }

    /**
     * @param httpSession session回话
     * @return 获取当前用户所在位置
     */
    public String getUserLocation(HttpSession httpSession) {
        userLocation = SessionUtil.getCurrentUserLocationBySession(httpSession);
        return userLocation;
    }


    /**
     * @param object 保存对象类型
     * @return 返回操作值对象
     */
    protected ReturnObject save(String objectName, Object object) {
        return commonDataService.getReturnType(object != null, objectName + "保存成功", objectName + "保存失败");
    }

    /**
     * @param requestMap
     * @return 获取排序的map
     * 第0位为排序顺序  asc desc
     * 第1位为排序字段
     */
    public Sort getSort(Map<String, String[]> requestMap) {
        String sortName = "id";
        Sort.Direction direction = Sort.Direction.ASC; //默认升序排列
        String directionStr = "asc";
        for (String key : requestMap.keySet()) {
            //如果key中以sort开头 获取它的值
            if (key.startsWith("sort")) {
                sortName = (key != null) ? key.split("\\[|\\]")[1] : sortName;
                directionStr = (requestMap.get(key)[0] != null) ? requestMap.get(key)[0] : directionStr;
                break;
            }
        }
        if (directionStr.equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = new Sort(direction, sortName);
        return sort;
    }


    /**
     * @param request
     * @return
     */
    public String getFilterStr(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return SessionUtil.getCurrentUserLocationBySession(session);
    }
}

