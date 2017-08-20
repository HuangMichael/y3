package com.subway.dao.userLog;

import com.subway.domain.log.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by huangbin on 2017-08-17 13:48:32
 * 用户日志信息数据接口
 */
public interface UserLogRepository extends JpaRepository<UserLog, Long>, CrudRepository<UserLog, Long> {


    /**
     * @param userName
     * @return 根据用户名称查询用户日志
     */
    List<UserLog> findByUserNameContains(String userName);


    /**
     * @param userName
     * @return 根据用户名称查询用户日志
     */
    Page<UserLog> findByUserNameContains(String userName, Pageable pageable);


}
