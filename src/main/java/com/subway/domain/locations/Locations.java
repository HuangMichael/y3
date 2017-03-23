package com.subway.domain.locations;


import com.subway.domain.line.Line;
import com.subway.domain.line.Station;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by huangbin on 2016/03/17 0023.
 * 设备位置信息
 */
@Entity
@Table(name = "T_LOCATIONS")
@Data
public class Locations implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 30, updatable = false)
    private String location;//位置
    @Column(length = 100)
    private String description;//描述

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", referencedColumnName = "id")
    Line line; //线路

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    Station station; //车站


    private Long parent;

    @Column(length = 1, nullable = false)
    private Long locLevel;


    @Column(length = 1, columnDefinition = "default 1") //默认位置正常
    private String status;//状态
    @Column(length = 1)
    private String hasChild;  //是否有孩子节点


    @Column(length = 1)
    private String locationType;  //位置类型  1段区 2站区

}