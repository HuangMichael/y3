package com.subway.domain.userLog;

import com.subway.dao.userLog.UserLogRepository;
import com.subway.domain.log.UserLog;

import com.subway.service.app.BaseService;
import com.subway.utils.search.SortedSearchable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangbin  on 2017/5/17.
 * <p>
 * 复合查询接口类
 */

@Service
public class UserLogSearchService extends BaseService implements SortedSearchable {

    @Autowired
    UserLogRepository userLogRepository;
    /**
     * @param searchPhrase
     * @return 根据多条件关键字进行查询
     */
    public List<UserLog> findByConditions(String searchPhrase, int paramSize) {
        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
        return userLogRepository.findByUserNameContains(array[0]);
    }


    /**
     * @param searchPhrase
     * @return 根据多条件关键字进行查询
     */
    public Page<UserLog> findByConditions(String searchPhrase, int paramSize, Pageable pageable) {
        String array[] = super.assembleSearchArray(searchPhrase, paramSize);
        return userLogRepository.findByUserNameContains(array[0], pageable);
    }


}
