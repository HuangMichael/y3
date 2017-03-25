package com.subway.dao.equipments;


import com.subway.domain.equipments.VeqClass;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by huangbin on 2016/3/15 0008.
 * 设备分类视图信息查询接口
 */
public interface VeqClassRepository extends CrudRepository<VeqClass, Long> {
    /**
     * 查询所有设备分类视图信息
     */
    List<VeqClass> findAll();

    /**
     * 根据id查询设备分类视图信息
     */
    VeqClass findById(long id);


    /**
     * 查询所有设备分类视图信息
     *
     * @param classType 根据位置类型查询
     * @return
     */
    List<VeqClass> findByClassType(String classType);


}
