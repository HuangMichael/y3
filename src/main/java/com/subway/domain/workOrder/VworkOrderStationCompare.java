package com.subway.domain.workOrder;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by HUANGBIN on 2017/5/13.
 * 工单数量按照线统计
 */


@Entity
@Table(name = "v_work_order_station_compare")
@Data
public class VworkOrderStationCompare {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String lineNo;
    private String stationNo;
    private String station;
    private Long num;
    private String reportMonth;
    private String name;
}
