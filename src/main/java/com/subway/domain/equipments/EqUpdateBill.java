package com.subway.domain.equipments;

import com.subway.domain.locations.Locations;
import lombok.*;

import javax.persistence.*;
import javax.xml.stream.Location;

/**
 * 采购申请单
 **/
@Entity
@Table(name = "T_EQ_RECORDS")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EqUpdateBill {

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

    @Column(length = 10)
    private String approver; //批准人

    @Column(length = 10)
    private String handler; //经办人

    @Column(length = 10)
    private String receiver; //接收人

    @Column(length = 20)
    private String eqCode; //设备编号

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipments equipments; //设备位置

    @Column(length = 1, columnDefinition = "default '2'")
    private String dataType; //数据分类 1为新置  2为更新

}
