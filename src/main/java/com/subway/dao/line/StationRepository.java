package com.subway.dao.line;


import com.subway.domain.line.Line;
import com.subway.domain.line.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by huangbin on 2016/3/15 0008.
 * 车站信息查询接口
 */
public interface StationRepository extends CrudRepository<Station, Long> ,JpaRepository<Station,Long>{
    /**
     * 查询所有车站
     */
    List<Station> findAll();

    /**
     * 根据状态查询车站
     */
    List<Station> findByStatus(String status);

    /**
     * 根据id查询
     */
    Station findById(long id);

    /**
     * 根据线路和使用状态查寻车站
     */

    List<Station> findByLineAndStatus(Line line, String status);


    /**
     * 根据id 状态查询车站
     */
    Station findByIdAndStatus(Long id, String status);


    /**
     * @param stationNo   编号
     * @param description 站名
     * @param pageable    可分页
     * @return 根据站名模糊查询
     */
    Page<Station> findByLineAndStationNoContainsAndDescriptionContains(Line line, String stationNo, String description, Pageable pageable);


    /**
     * @param stationNo   编号
     * @param description 站名
     * @return 根据站名模糊查询
     */
    List<Station> findByLineAndStationNoContainsAndDescriptionContains(Line line, String stationNo, String description);


    /**
     * @return 查询所有的id
     */
    @Query("select u.id from Station u")
    List<Long> findAllIds();


    /**
     * @param stationNo 站编号
     * @return 按照编号查询站
     */

    List<Station> findByStationNo(String stationNo);
}
