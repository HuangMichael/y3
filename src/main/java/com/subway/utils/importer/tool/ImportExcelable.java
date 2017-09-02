package com.subway.utils.importer.tool;


import com.subway.domain.etl.EtlTable;
import com.subway.domain.etl.EtlTableConfig;

import java.io.IOException;
import java.util.List;

/**
 * Created by huangbin on  2017-08-16 16:27:19
 * 文档导入接口
 */
public interface ImportExcelable {

    /**
     * @param etlTable
     * @param filePath           文档路径
     * @param className          类名
     * @param startRow           开始行 默认0
     * @param etlTableConfigList
     * @return
     * @throws IOException
     */
    int[] importDataFromExcel(EtlTable etlTable, String filePath, String className, Integer startRow, List<EtlTableConfig> etlTableConfigList) throws IOException;


    /**
     * @param etlTable
     * @param etlTableConfigList
     * @return
     * @throws IOException
     */
    int[] importDataToBaseTable(EtlTable etlTable, List<EtlTableConfig> etlTableConfigList) throws IOException;

}
