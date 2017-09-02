package com.subway.dao.etl;


import com.subway.domain.etl.EtlTableTree;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/8/16.
 */
public interface EtlTableTreeRepository  extends JpaRepository<EtlTableTree, Long> {
}
