package com.subway.domain.equipments;


import com.subway.domain.locations.Vlocations;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by huangbin on 2016/03/14 0023.
 * 设备信息
 */
@Entity
@Table(name = "T_EQUIPMENTS")
@Data
public class Equipments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;  //id

    @Column(length = 20, unique = true)
    private String eqCode; //设备编号


    @Column(length = 20)
    private String description; //设备描述


    @Column(length = 20)
    private String manager; //设备负责人员


    @Column(length = 20)
    private String maintainer; //设备维护人员

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate; //购置日期


    @Column(length = 50)
    private String productFactory; //生产厂家


    @Column(scale = 2)
    private Double originalValue; //原值


    @Column(scale = 2)
    private Double netValue; //净值

//    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "locations_id", referencedColumnName = "id")
//    private Locations locations;  //所属位置


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vlocations_id", referencedColumnName = "id")
    private Vlocations vlocations;  //所属位置

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipments_Classification_id", referencedColumnName = "id")
    private EquipmentsClassification equipmentsClassification; //设备分类


    @Column(length = 1, columnDefinition = "default 1")
    private String status; //默认为正常  0不正常 1正常  2报修   3报废

    @Column(length = 20)
    private String location; //加入冗余字段location 方便模糊查询


    @Column(length = 50)
    private String eqModel;  //设备型号
    @Column(length = 20)
    private String eamNo;  //EAM编号

    @Column(length = 1)
    private Long manageLevel; //管理等级

    @Column(length = 1, columnDefinition = "default 1")
    private String running; //是否运行

    @Column(scale = 2)
    private Double purchasePrice; //采购价格

    private String warrantyPeriod; //保修期至

    private String setupDate; //安装日期

    private String runDate; //运行日期

    private String productDate; //出厂日期
    @Column(length = 2)
    private Long expectedYear; //预计年限

}
