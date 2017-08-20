package com.subway.domain.log;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志信息表
 *
 * @author HUANGBIN
 */
@Entity
@Table(name = "T_USER_LOG")
@Data
public class UserLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 11, nullable = false)
    private Long id; //序号

    @Column(length = 45, nullable = false)
    private String userName; //用户名

    @Column(length = 20)
    private String operation; //操作描述

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationTime; //最近登陆时间

    @Column(length = 20)
    private String loginIp; //最近登陆ip

    @Column(length = 10)
    private String status; //用户状态


}