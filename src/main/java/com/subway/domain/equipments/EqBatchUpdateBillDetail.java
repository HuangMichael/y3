package com.subway.domain.equipments;

import lombok.Data;

import javax.persistence.*;

/**
 * 设备批量更新明细信息
 **/
@Entity
@Table(name = "T_EQ_BATCH_UPDATE_Bill_DETAIL")
@Data
public class EqBatchUpdateBillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipments_id", referencedColumnName = "id")
    private Equipments  equipments;

    @Column(length = 100)
    private String memo; //备注信息

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private EqBatchUpdateBill bill;//设备分类

    @Column(length = 1, columnDefinition = "default 1")
    private String status; //类型

}
