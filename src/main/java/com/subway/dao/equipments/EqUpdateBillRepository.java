package com.subway.dao.equipments;

import com.subway.domain.equipments.EqUpdateBill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 设备更新申请单数据接口
 *
 * @Date 2016年9月23日10:46:47
 **/
public interface EqUpdateBillRepository extends PagingAndSortingRepository<EqUpdateBill, Long>, CrudRepository<EqUpdateBill, Long> {

    /**
     * @param id 根据id查询
     * @return
     */
    EqUpdateBill findById(Long id);


    /**
     * @return 查询所有的新置的id
     */
    @Query("select v.id from VEqRecord v where v.dataType ='更新'")
    List<Long> findAllIds();


    /**
     * @param id 设备id
     * @return 根据设备id查询设备的更新历史
     */
    @Query("select ub from EqUpdateBill ub where ub.equipments.id =:id order By ub.id desc")
    List<EqUpdateBill> findByEquipmentsId(@Param("id") Long id);
}
