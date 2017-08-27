package com.subway.dao.user;


import com.subway.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by huangbin on 2016/1/8 0008.
 */
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    /**
     * 查询所有菜单
     */
    List<User> findAll();

    /**
     * 根据状态查询所有菜单
     */
    List<User> findByStatus(String status);


    /**
     * @param userName 根据用户名查询
     * @return
     */
    User findByUserName(String userName);

    /**
     * 根据id查询
     */
    User findById(long id);

    /**
     * 根据用户名和密码查询用户
     */
    User findByUserNameAndPasswordAndStatus(String userName, String password, String status);


    User save(User user);

    /**
     * @param userName 用户名
     * @param status   用户状态
     * @return
     */
    List<User> findByUserNameAndStatus(String userName, String status);


    /**
     * 查询不在当前角色中的用户信息
     */
    @Query(nativeQuery = true, value = "SELECT  u.id,u.user_name FROM t_user u WHERE u.id NOT IN (SELECT ur.user_id FROM  t_role_user ur  WHERE ur.role_id = :roleId) AND u.status = 1")
    List<Object> findUsersNotInRole(@Param("roleId") Long roleId);


    /**
     * 查询不在当前位置中的用户信息
     */
    @Query(nativeQuery = true, value = "SELECT  u.id,u.user_name FROM t_user u WHERE u.vlocations_id != :locationId AND u.status = 1")
    List<Object> findUsersNotInLocation(@Param("locationId") Long locationId);


    /**
     * 查询在当前位置中的用户信息
     */
    @Query(nativeQuery = true, value = "SELECT  u.id,u.user_name FROM t_user u WHERE u.vlocations_id = :locationId AND u.status = 1")
    List<Object> findUsersInLocation(@Param("locationId") Long locationId);

    /**
     * @param roleId
     * @return
     */
    @Query(value = "select r.userList from Role r where r.id = :roleId")
    List<User> findUserListByRoleId(@Param("roleId") Long roleId);


    /**
     * @return 查找所有的id
     */
    @Query("select e.id from User e order by e.id asc")
    List<Long> findAllId();


    /**
     * @param userName
     * @param locName
     * @return
     */
    List<User> findByUserNameContainsAndVlocations_LocNameContains(String userName, String locName);


    /**
     * @param userName
     * @param locName
     * @param pageable
     * @return
     */
    Page<User> findByUserNameContainsAndVlocations_LocNameContains(String userName, String locName, Pageable pageable);


}
