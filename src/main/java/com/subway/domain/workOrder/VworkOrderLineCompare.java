package com.subway.domain.workOrder;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by HUANGBIN on 2017/5/13.
 * 工单数量按照线统计
 */


@Entity
@Table(name = "v_work_order_line_compare")
@Data
public class VworkOrderLineCompare {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String line;
    private String name;
    private String drillDown;
    private Long y;
    private String reportMonth;
    private String status;
}
