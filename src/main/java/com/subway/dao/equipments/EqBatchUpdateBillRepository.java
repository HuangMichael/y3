package com.subway.dao.equipments;

import com.subway.domain.equipments.EqBatchUpdateBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EqBatchUpdateBillRepository extends JpaRepository<EqBatchUpdateBill, Long>,CrudRepository<EqBatchUpdateBill, Long> {


    /**
     * @param eqClassDesc 设备分类描述
     * @param locDesc     位置描述
     * @param pageable    分页参数
     * @return
     */
    Page<EqBatchUpdateBill> findByEquipmentsClassification_CnameContainsAndLocation_LocNameContains(String eqClassDesc, String locDesc, Pageable pageable);


    /**
     * @param eqClassDesc 设备分类描述
     * @param locDesc     位置描述
     * @return
     */
    List<EqBatchUpdateBill> findByEquipmentsClassification_CnameContainsAndLocation_LocNameContains(String eqClassDesc, String locDesc);


    /**
     * @param eqBatchUpdateBill
     * @return
     */
    EqBatchUpdateBill save(EqBatchUpdateBill eqBatchUpdateBill);

}
