package com.subway.service.etl;

import com.subway.dao.etl.EtlRepository;
import com.subway.domain.etl.EtlTable;
import com.subway.domain.etl.EtlTableConfig;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by huangbin on 2017/8/16.
 */
@Service
public class EtlService extends BaseService {
    @Autowired
    EtlRepository etlRepository;

    @Autowired
    CommonDataService commonDataService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    EtlTableService etlTableService;

    /**
     * @param etlTableConfig
     * @return 保存元数据表属性
     */
    public EtlTableConfig save(EtlTableConfig etlTableConfig) {
        return etlRepository.save(etlTableConfig);
    }

    /**
     * @return 查询所有的元数据表属性
     */
    public List<EtlTableConfig> findAll() {
        return etlRepository.findAll();
    }

    /**
     * @param id
     * @return 根据id查询元数据表属性
     */
    public EtlTableConfig findById(Long id) {
        return etlRepository.getOne(id);
    }

    /**
     * @param id
     * @return 根据id删除
     */
    public ReturnObject delete(Long id) {
        EtlTableConfig etlTableConfig = etlRepository.getOne(id);
        if (etlTableConfig == null) {
            return commonDataService.getReturnType(etlTableConfig != null, "", "id为" + id + "的ETL元数据属性信息不存在,请确认！");
        }

        try {
            etlRepository.delete(etlTableConfig);
            //再查询一次查看是否删除
            EtlTableConfig etlTableConfig1 = etlRepository.getOne(id);
            return commonDataService.getReturnType(etlTableConfig1 == null, "ETL元数据属性信息删除成功！", "ETL元数据属性信息删除失败！");
        } catch (Exception e) {
            e.printStackTrace();
            return commonDataService.getReturnType(false, "", "ETL元数据属性信息删除失败！");
        }
    }

    /**
     * @return 选择所有的id列表
     */
    public List<Long> selectAllIds() {
        return etlRepository.selectAllIds();
    }

    /**
     * @param etlTableId
     * @return 根据etlTableId查询该表的所有属性信息EtlTableConfig
     */
    public List<EtlTableConfig> findByEtlTableId(Long etlTableId) {
        return etlRepository.findByTableIdAndStatus(etlTableId, "1");
    }


    /**
     * @param tableName 判断表在数据库中是否存在
     * @return
     */
    public boolean isTableExist(String tableName) throws Exception {
        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection connection;
        ResultSet rs;
        connection = dataSource.getConnection();
        rs = connection.getMetaData().getTables(null, null, tableName, null);
        return rs.next();
    }

    /**
     * @param etlTable           etl表信息
     * @param etlTableConfigList etl基础表配置
     */
    public void createBaseTable(String schemaName, EtlTable etlTable, List<EtlTableConfig> etlTableConfigList) {
        StringBuilder sql = new StringBuilder();
        String baseTableName = etlTable.getBaseTableName();
        sql.append("create table " + schemaName + "." + baseTableName);
        sql.append("(");
        sql.append(etlTableService.getCreateTableContent(etlTableConfigList));
        //组装列配置
        sql.append(") with (oids =false);");
        sql.append("ALTER TABLE " + schemaName + "." + baseTableName + " ADD PRIMARY KEY (id)");
        log.info("sql---------------" + sql);
        jdbcTemplate.execute(sql.toString());

    }


    /**
     * @param schemaName
     * @param etlTable
     */
    public void dropBaseTable(String schemaName, EtlTable etlTable) {
        String sql = "";
        String baseTableName = etlTable.getBaseTableName();
        if (etlTable.getDropStatus().equals("1")) {
            sql += " drop table if exists " + schemaName + "." + baseTableName + ";";
            log.info("dropBaseTable sql---------------" + sql);
            jdbcTemplate.execute(sql);
        }

    }


    /**
     * @param schemaName         模式
     * @param etlTable           etl表信息
     * @param etlTableConfigList etl基础表配置
     *                           导入业务数据sql
     */
    public void importServiceDataSql(String schemaName, EtlTable etlTable, List<EtlTableConfig> etlTableConfigList) {
        StringBuilder sql = new StringBuilder();
        String baseTableName = etlTable.getBaseTableName();
        String insertCols = etlTableService.getInsertDataCols(etlTableConfigList);
        String serviceTableName = etlTable.getServiceTableName();
        sql.append("insert into " + schemaName + "." + serviceTableName);
        sql.append(insertCols);
        sql.append("select " + etlTableService.getSelectDataCols(etlTableConfigList) + " from " + baseTableName);
        sql.append(etlTableService.getJoinCondition(etlTable, etlTableConfigList));
        log.info("sql------importServiceDataSql---------" + sql);
        jdbcTemplate.execute(sql.toString());
    }


    /**
     * @param tableName 判断表在数据库中是否存在
     * @return
     */
    public boolean getTableConfig(String tableName) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            Connection connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet colRet = databaseMetaData.getColumns(connection.getCatalog(), "%", tableName, "%");
            while (colRet.next()) {
                String columnName = colRet.getString("COLUMN_NAME");
                String columnType = colRet.getString("TYPE_NAME");
                int dataSize = colRet.getInt("COLUMN_SIZE");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int nullable = colRet.getInt("NULLABLE");
                System.out.println(columnName + " " + columnType + " " + dataSize + " " + digits + " " + nullable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}