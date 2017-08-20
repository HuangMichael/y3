package com.subway.dao.workOrder;

import com.subway.domain.workOrder.VworkOrderReportBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by huangbin on 2016/9/2.
 */
public interface VworkOrderReportBillRepository extends PagingAndSortingRepository<VworkOrderReportBill, Long>, JpaSpecificationExecutor<VworkOrderReportBill> {

    List<VworkOrderReportBill> findAll();

    @Query("SELECT count(r) from VworkOrderReportBill r  ")
    Long selectCount();


    //多字段模糊查询


    /**
     * @param orderDesc 报修描述
     * @param locName   位置名称
     * @param eqName
     * @param pageable
     * @return 模糊查询
     */
    Page<VworkOrderReportBill> findByOrderDescContainsOrLocNameContainsOrEqNameContains(String orderDesc, String locName, String eqName, Pageable pageable);


    /**
     * @param orderDesc 报修描述
     * @param locName   位置名称
     * @param eqName
     * @return 模糊查询
     */
    List<VworkOrderReportBill> findByOrderDescContainsOrLocNameContainsOrEqNameContains(String orderDesc, String locName, String eqName);

    /**
     * @param orderDesc 报修描述
     * @param pageable  分页
     * @return
     */
    Page<VworkOrderReportBill> findByOrderDesc(String orderDesc, Pageable pageable);


    Page<VworkOrderReportBill> findByLocNameContains(String locName, Pageable pageable);


    /**
     * @param locName 按位置不分页查询
     * @return
     */
    List<VworkOrderReportBill> findByLocNameContains(String locName);


    /**
     * @param
     * @return 根据位置和节点的状态查询
     */
    Page<VworkOrderReportBill> findByNodeState(String nodeState, Pageable pageable);


    /**
     * @param
     * @return 根据位置和节点的状态查询
     */
    List<VworkOrderReportBill> findByNodeState(String nodeState);


    /**
     * @param location
     * @param orderLineNo
     * @param orderDesc 报修描述
     * @param eqClass
     * @param locName
     * @param pageable
     * @return 模糊查询
     */
    Page<VworkOrderReportBill> findByLocationContainsAndOrderLineNoContainsAndOrderDescContainsAndLocNameContainsAndEqClassContainsOrderByNodeTimeDesc(String location,String orderLineNo, String orderDesc, String eqClass, String locName, Pageable pageable);


    /**
     * @param location
     * @param orderLineNo
     * @param orderDesc 报修描述
     * @param locName
     * @param eqClass
     * @return 模糊查询
     */
    List<VworkOrderReportBill> findByLocationContainsAndOrderLineNoContainsAndOrderDescContainsAndLocNameContainsAndEqClassContainsOrderByNodeTimeDesc(String location,String orderLineNo, String orderDesc, String locName, String eqClass);


}
