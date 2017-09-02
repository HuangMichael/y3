package com.subway.dao.etl;

import com.subway.domain.etl.EtlTableConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.OrderBy;
import java.util.List;

/**
 * Created by huangbin on 2017/8/16.
 */
public interface EtlRepository extends JpaRepository<EtlTableConfig, Long> {
    /**
     * @return 查询所有的id
     */
    @Query("select a.id from EtlTableConfig a ")
    List<Long> selectAllIds();

    /**
     * @param searchPhrase
     * @return 根据多条件关键字进行查询
     */
    List<EtlTableConfig> findByBaseColDescContains(String searchPhrase);

    /**
     * @param searchPhrase
     * @param pageable
     * @return 根据多条件关键字进行查询
     */
    Page<EtlTableConfig> findByBaseColDescContains(String searchPhrase, Pageable pageable);

    /**
     * @param etlTableId
     * @return 根据etlTableId查询该表的所有属性信息EtlTableConfig
     */
    @OrderBy("sortNo")
    List<EtlTableConfig> findByTableIdAndStatus(Long etlTableId, String status);
}
