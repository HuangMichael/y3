package com.subway.service.user;

import com.subway.dao.locations.VlocationsRepository;
import com.subway.dao.person.PersonRepository;
import com.subway.dao.user.UserRepository;
import com.subway.domain.locations.Vlocations;
import com.subway.domain.person.Person;
import com.subway.domain.user.User;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import com.subway.utils.CommonStatusType;
import com.subway.utils.MD5Util;
import com.subway.utils.RedisUtils;
import com.subway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin  on 2016/4/14.
 */

@Service
@CacheConfig
public class UserService extends BaseService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    VlocationsRepository vlocationsRepository;

    @Autowired
    CommonDataService commonDataService;

    /**
     * 根据状态查询用户
     */
    @Cacheable(key = "'userList'", value = "userList")
    public List<User> findByStatus(String status) {
        return userRepository.findByStatus(status);
    }


    /**
     * 根据状态查询用户
     */
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * @return 查询所有的用户
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 对用户进行加密
     */
    public User save(User user) {

        if (user.getPassword() == null) {
            user.setPassword(MD5Util.md5("123456"));
        }
    /*    if (user.getLocation() == null) {
           // System.out.println("user.getVlocations()-----" + user.getVlocations().getLocName());
            user.setLocation("BJ");
        }*/
        if (user.getSortNo() == 0) {
            user.setSortNo(1l);
        }
        user.setStatus("1");

        user = userRepository.save(user);
        Object str = RedisUtils.get("userList");
        if (str != null) {
            log.info("userList" + str.toString());
            RedisUtils.del("userList");
        }
        return user;
    }

    /**
     * 对用户进行加密
     */
    public User createUser(User user) {
        user.setPassword("123456");
        String password = user.getPassword();
        if (user.getPassword() != null) {
            user.setPassword(MD5Util.md5(password));
        }
        user.setStatus("1");
        return userRepository.save(user);
    }


    /**
     * 对用户进行加密
     */
    public User update(Long userId, Long personId, Long locationId, String status) {
        User user = userRepository.findById(userId);
        Person person = personRepository.findById(personId);
        Vlocations vlocations = vlocationsRepository.findById(locationId);
        user.setPerson(person);
        user.setVlocations(vlocations);
        // user.setLocation(vlocations.getLocation());
        user.setStatus(status);
        return userRepository.save(user);
    }


    /**
     * @param userName 用户名
     * @param password 密码密文
     * @param status   用户状态
     * @return
     */
    public ReturnObject findByUserNameAndPasswordAndStatus(HttpServletRequest request, String userName, String password, String status) {
        User userFound = userRepository.findByUserName(userName);
        String failMessage = "";
        Boolean result = false;
        User user = null;
        if (userFound == null) {
            failMessage = "用户名不存在，请联系管理员!";
        } else {
            user = userFound;
            if (user.getPassword() == null || user.getStatus() == null) {
                failMessage = "用户密码或状态为空,请重新输入";
            } else if (user.getPassword() != null && !user.getPassword().equals(password)) {
                failMessage = "用户密码有误,请重新输入";
            } else if (user.getStatus() != null && !user.getStatus().equals(status)) {
                failMessage = "用户信息为锁定状态,请联系管理员!";
            } else {
                result = true;
                //将用户人员信息放入session.
                HttpSession session = request.getSession();
                Person person = user.getPerson();
                session.setAttribute("currentUser", user);
                session.setAttribute("person", person);
            }
        }
        return commonDataService.getReturnType(result, "用户登录成功", failMessage);
    }


    /**
     * @param userName
     * @param password
     * @param status
     * @return
     */
    public User findByUserNameAndPasswordAndStatus(String userName, String password, String status) {
        return userRepository.findByUserNameAndPasswordAndStatus(userName, password, status);
    }


    /**
     * @param userName 用户名
     * @param status   状态
     * @return
     */
    public User findByUserNameAndStatus(String userName, String status) {
        List<User> userList = userRepository.findByUserNameAndStatus(userName, status);
        User user;
        if (!userList.isEmpty()) {
            user = userList.get(0);
        } else {
            user = null;
        }
        return user;
    }

    /**
     * 修改用户密码
     */
    public boolean changePwd(String userName, String password) {
        List<User> userList = userRepository.findByUserNameAndStatus(userName, CommonStatusType.STATUS_YES);
        if (!userList.isEmpty()) {
            User user = userList.get(0);
            user.setPassword(MD5Util.md5(password));
            user = userRepository.save(user);
            return user != null;
        } else {
            return false;
        }
    }


    public List<Long> selectAllId() {

        return userRepository.findAllId();
    }

    /**
     * @param locationId 对位置locationId对人员进行授权
     * @param userIds
     * @return
     */
    public ReturnObject grantDataAuth(Long locationId, String userIds) {
        Vlocations vlocations = vlocationsRepository.findById(locationId);
        String userIdArray[] = userIds.split(",");
        Long userId = null;
        User user = null;
        List<User> users = new ArrayList<User>();
        for (String str : userIdArray) {
            userId = Long.parseLong(str);
            user = userRepository.findById(userId);
            user.setVlocations(vlocations);
            userRepository.save(user);
            users.add(user);
        }
        String msg = vlocations.getLocName() + users.size() + "个用户";
        return commonDataService.getReturnType(!users.isEmpty(), msg + "数据授权成功", "数据授权失败，请重试");
    }


    /**
     * @param locationId
     * @return 查询不在当前位置的用户信息
     */
    public List<Object> findUsersNotInLocation(Long locationId) {
        return userRepository.findUsersNotInLocation(locationId);
    }


    /**
     * @param locationId
     * @return 查询在当前位置的用户信息
     */
    public List<Object> findUsersInLocation(Long locationId) {
        return userRepository.findUsersInLocation(locationId);
    }


    /**
     * @param userId 用户id
     * @return 移除用户数据授权
     */
    public Boolean removeLoc(Long userId) {
        boolean result = false;
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setVlocations(null);
            user = userRepository.save(user);
            result = user.getVlocations() == null;
        }
        return result;
    }

}
