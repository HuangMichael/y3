package com.subway.utils.importer.tool;


import com.subway.domain.etl.EtlTable;
import com.subway.domain.etl.EtlTableConfig;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 导入实现业务类
 **/
@Service
public class ImportExcelService implements ImportExcelable {
    @Autowired
    JdbcTemplate jdbcTemplate;
    protected Log log = LogFactory.getLog(this.getClass());

    /**
     * @param filePath           excel文档的路径
     * @param className          类名
     * @param startRow           开始行
     * @param etlTableConfigList 对应的字段配置列表
     * @return
     * @throws IOException
     */
    public int[] importDataFromExcel(EtlTable etlTable, String filePath, String className, Integer startRow, List<EtlTableConfig> etlTableConfigList) throws IOException {
        int[] size = {};
        try {
            InputStream is = new FileInputStream(filePath);
            Workbook rwb = Workbook.getWorkbook(is);
            Sheet rs = rwb.getSheet(0);
            int rowNum = rs.getRows();
            Object object;
            startRow = (startRow != null) ? startRow : 0;
            //从第一行开始  跳过标题行
            EtlTableConfig config;
            String sql = "";
            sql += "insert into  " + etlTable.getBaseTableName();
            sql += "(";
            sql += "id,";
            for (int c = 0; c < etlTableConfigList.size(); c++) {
                config = etlTableConfigList.get(c);
                sql += config.getBaseColName() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
            sql += "values";
            for (int r = startRow; r < rowNum; r++) {
                sql += "(nextval('seq_area'),";
                for (int c = 0; c < etlTableConfigList.size(); c++) {
                    sql += "'" + rs.getCell(c, r).getContents().trim() + "',";
                    log.info("content----------" + rs.getCell(c, r).getContents());
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += "),";
            }
            sql = sql.substring(0, sql.length() - 1);
            log.info("sql-----importDataFromExcel-----" + sql);
            size = jdbcTemplate.batchUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * @param etlTable           etl表信息
     * @param etlTableConfigList etl表配置信息
     * @return
     * @throws IOException
     */
    public int[] importDataToBaseTable(EtlTable etlTable, List<EtlTableConfig> etlTableConfigList) throws IOException {
        String sql = "";
        //组装sql语句
        sql += "insert into table (colsStr) values";
        sql += "(";
        sql += ")";
        int size[] = jdbcTemplate.batchUpdate(sql);
        return size;
    }
}
