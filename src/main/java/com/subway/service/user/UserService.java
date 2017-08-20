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
import com.subway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbin  on 2016/4/14.
 */

@Service
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
        return userRepository.save(user);
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
     * 查询加密后的用户
     */

    public List<User> findByUserNameAndPasswordAndStatus(String userName, String password, String status) {

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


//    /**
//     * @param searchPhrase
//     * @return 根据多条件关键字进行查询
//     *//*
//    public List<User> findByConditions(String searchPhrase, int paramSize) {
//        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
//        return userRepository.findByUserNameContainsAndLocationStartingWith(array[0], array[1]);
//    }
//
//
//    *//**
//     * @param searchPhrase
//     * @return 根据多条件关键字进行查询
//     *//*
//    public Page<User> findByConditions(String searchPhrase, int paramSize, Pageable pageable) {
//        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
//        return userRepository.findByUserNameContainsAndLocationStartingWith(array[0], array[1], pageable);
//    }
//
//
//    */
//
//    /**
//     * @param searchPhrase
//     * @return 根据多条件关键字进行查询 可排序
//     *//*
//    public List<User> findByConditions(String searchPhrase, String[] sortStr, int paramSize) {
//        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
//        Sort sort = super.assembleSort(sortStr);
//        return userRepository.findByUserNameContainsAndLocationStartingWith(array[0], array[1], sort);
//    }
//
//
//
//    public Page<User> findByConditions(String searchPhrase, int paramSize, String[] sortStr, Pageable pageable) {
//        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
//        Sort sort = super.assembleSort(sortStr);
//        return userRepository.findByUserNameContainsAndLocationStartingWith(array[0], array[1], sort, pageable);
//    }*/
//


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
