package com.subway.service.budge;

import com.subway.dao.budget.VbudgetBillRepository;
import com.subway.domain.budget.VbudgetBill;
import com.subway.service.app.BaseService;
import com.subway.utils.DateUtils;
import com.subway.utils.search.SortedSearchable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by huangbin on 2016/5/4.
 * 设备台账业务类
 */
@Service
public class BudgeSearchService extends BaseService implements SortedSearchable {


    @Autowired
    VbudgetBillRepository budgetBillRepository;

    /**
     * @param searchPhrase
     * @param paramsSize
     * @return
     */
    public List<VbudgetBill> findByConditions(String searchPhrase, int paramsSize) {
        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        Date beginDate = null, endDate = null;
        if (array[0].isEmpty()) {
            beginDate = new Date();
        } else {
            try {
                beginDate = DateUtils.convertStr2Date(array[0], "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (array[1].isEmpty()) {
            endDate = new Date();
        } else {
            try {
                endDate = DateUtils.convertStr2Date(array[1], "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return budgetBillRepository.findByApplyDateBetweenAndAccessoryNameContainsAndApplyDepContains(beginDate, endDate, array[2], array[3]);
    }

    /**
     * @param searchPhrase
     * @param paramsSize
     * @return
     */
    public Page<VbudgetBill> findByConditions(String searchPhrase, int paramsSize, Pageable pageable) {
        String array[] = super.assembleSearchArray(searchPhrase, paramsSize);
        Date beginDate = null, endDate = null;
        if (array[0].isEmpty()) {
            beginDate = new Date();
        } else {
            try {
                beginDate = DateUtils.convertStr2Date(array[0], "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (array[1].isEmpty()) {
            endDate = new Date();
        } else {
            try {
                endDate = DateUtils.convertStr2Date(array[1], "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return budgetBillRepository.findByApplyDateBetweenAndAccessoryNameContainsAndApplyDepContains(beginDate, endDate, array[2], array[3], pageable);
    }


}
