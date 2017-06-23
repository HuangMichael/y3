package com.subway.domain.equipments;

import com.subway.domain.locations.Locations;
import com.subway.domain.locations.Vlocations;
import lombok.Data;

import javax.persistence.*;

/**
 * 设备批量更新申请单
 **/
@Entity
@Table(name = "T_EQ_BATCH_UPDATE_BILL")
@Data
public class EqBatchUpdateBill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  //id

    @Column(length = 20)
    private String applyDate;// 申购日期
    @Column(length = 50)
    private String applicant; //申请人

    @Column(length = 50)
    private String applyDep; //申请部门

    @Column(length = 50)
    private String purpose; //用途

    @Column(length = 50)
    private String model; //设备型号

    @Column(length = 10)
    private String approver; //批准人

    @Column(length = 10)
    private String handler; //经办人

    @Column(length = 10)
    private String receiver; //接收人

    @Column(length = 10)
    private String dataType; //类型


    @Column(length = 100)
    private String billContent; //内容

//    @ManyToOne
//    @JoinColumn(name = "eq_class_id", referencedColumnName = "id")
//    private VeqClass equipmentsClassification; //位置

//    @ManyToOne
//    @JoinColumn(name = "location_id", referencedColumnName = "id")
//    private Vlocations location; //位置

    @Column(length = 1,columnDefinition = "default 1")
    private String status; //类型

}
