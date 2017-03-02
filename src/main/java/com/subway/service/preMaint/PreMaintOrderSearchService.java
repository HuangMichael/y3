package com.subway.service.preMaint;

import com.subway.dao.preMaint.VpreMaintOrderRepository;
import com.subway.domain.preMaint.VpreMaintOrder;
import com.subway.service.app.BaseService;
import com.subway.utils.search.SortedSearchable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangbin on 2016年10月9日11:46:27
 * 预防性维修查询业务类
 */
@Service
@Data
public class PreMaintOrderSearchService extends BaseService implements SortedSearchable {


    @Autowired
    VpreMaintOrderRepository orderRepository;

    /**
     * @param searchPhrase
     * @param paramsSize
     * @return
     */
    public List<VpreMaintOrder> findByConditions(String searchPhrase, int paramsSize) {
        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        return orderRepository.findByOrderDescContainingAndLocationContainingAndNodeState(array[0], array[1], array[2]);
    }

    /**
     * @param searchPhrase
     * @param paramsSize
     * @return
     */
    public Page<VpreMaintOrder> findByConditions(String searchPhrase, int paramsSize, Pageable pageable) {
        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        return orderRepository.findByOrderDescContainingAndLocationContainingAndNodeState(array[0], array[1], array[2], pageable);
    }


}
