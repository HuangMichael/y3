package com.subway.domain.workOrder;

import com.subway.object.statistics.ValueObject;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Created by HUANGBIN on 2017/5/13.
 * 工单数量按照线统计
 */

@Data
public class VworkOrderStationCompareVo {
    private String id;
    private String name;
    private List<ValueObject> data;
}
