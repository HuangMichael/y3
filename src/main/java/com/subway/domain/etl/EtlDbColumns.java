package com.subway.domain.etl;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by huangbin on 2017/9/3.
 * 数据库配置表信息
 */
@Data
public class EtlDbColumns implements Serializable {
    private String tableName; //表名称
    private String columnName;//列名称
    private String columnType;//列数据类型
    private String columnKey;//主外键
    private String columnComment;//列注释
    private String isNullable;//是否为空
}

