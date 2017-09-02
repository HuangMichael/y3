package com.subway.domain.etl;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/20.
 */
@Entity
@Table(name = "T_ETL_MEDIA")
@Data
public class EtlMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11, nullable = false)
    private Long id; //序号

    @Column(length = 50, nullable = false)
    private String fileName; //多媒体文件名称

    @Column(nullable = false)
    private Long fileSize; //多媒体文件大小，单位为B

    @Column(length = 100, nullable = false)
    private String fileRelativeUrl; //多媒体文件相对路径

    @Column(length = 100, nullable = false)
    private String fileAbsoluteUrl; //多媒体文件绝对路径，精确到盘符

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etl_table_id", referencedColumnName = "id")
    private EtlTable etlTable;//所属基本信息表

    @Column(length = 100)
    private String memo; //备注信息

    @Column(length = 1)
    private String status; //状态
}
