package com.subway.service.etl;


import com.subway.dao.etl.EtlTableRepository;
import com.subway.domain.etl.EtlTable;
import com.subway.service.app.BaseService;
import com.subway.utils.search.SortedSearchable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HUANGBIN on 2017/8/16.
 */
@Service
public class EtlTableSearchService extends BaseService implements SortedSearchable {
    @Autowired
    EtlTableRepository etlTableRepository;

    /**
     * @param searchPhrase
     * @return 根据多条件关键字进行查询
     */
    public List<EtlTable> findByConditions(String searchPhrase, int paramSize) {
        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
        return etlTableRepository.findByBaseTableNameContains(array[0]);
    }


    /**
     * @param searchPhrase
     * @return 根据多条件关键字进行查询
     */
    public Page<EtlTable> findByConditions(String searchPhrase, int paramSize, Pageable pageable) {
        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
        return etlTableRepository.findByBaseTableNameContains(array[0],pageable);
    }
}
