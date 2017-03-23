package com.subway.domain.equipments;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by huangbin on 2016/03/14 0023.
 * 设备分类视图信息
 */
@Entity
@Table(name = "V_EQ_CLASS")
@Data
public class VeqClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 50,name = "cname")
    private String cname;
    @Column(length = 1)
    private String classType;
}

