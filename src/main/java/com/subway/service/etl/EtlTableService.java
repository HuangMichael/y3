package com.subway.service.etl;

/**
 * Created by huangbin on 2017/8/16.
 * ETL元数据业务类
 */

import com.subway.dao.etl.EtlMediaRepository;
import com.subway.dao.etl.EtlTableRepository;
import com.subway.dao.etl.EtlTableTreeRepository;
import com.subway.domain.etl.EtlMedia;
import com.subway.domain.etl.EtlTable;
import com.subway.domain.etl.EtlTableConfig;
import com.subway.domain.etl.EtlTableTree;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.commonData.CommonDataService;
import com.subway.utils.SessionUtil;
import com.subway.utils.UploadUtil;
import com.subway.utils.importer.tool.ImportExcelService;
import com.subway.utils.importer.tool.ImportService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class EtlTableService extends BaseService {
    @Autowired
    EtlTableRepository etlTableRepository;

    @Autowired
    EtlTableTreeRepository etlTableTreeRepository;


    @Autowired
    EtlMediaRepository etlMediaRepository;

    @Autowired
    CommonDataService commonDataService;

    @Autowired
    EtlService etlService;

    @Autowired
    ImportExcelService importService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param etlTable
     * @return 保存元数据表
     */
    public EtlTable save(EtlTable etlTable) {
        return etlTableRepository.save(etlTable);
    }

    /**
     * @return 查询所有的元数据表
     */
    public List<EtlTable> findAll() {
        return etlTableRepository.findAll();
    }

    /**
     * @param id
     * @return 根据id查询元数据表
     */
    public EtlTable findById(Long id) {
        return etlTableRepository.getOne(id);
    }

    /**
     * @param id
     * @return 根据id删除
     */
    public ReturnObject delete(Long id) {
        EtlTable etlTable = etlTableRepository.getOne(id);
        if (etlTable == null) {
            return commonDataService.getReturnType(etlTable != null, "", "id为" + id + "的ETL元数据信息不存在,请确认！");
        }

        try {
            etlTableRepository.delete(etlTable);
            //再查询一次查看是否删除
            EtlTable etlTable1 = etlTableRepository.getOne(id);
            return commonDataService.getReturnType(etlTable1 == null, "ETL元数据信息删除成功！", "ETL元数据信息删除失败！");
        } catch (Exception e) {
            e.printStackTrace();
            return commonDataService.getReturnType(false, "", "ETL元数据信息删除失败！");
        }
    }

    /**
     * @return 选择所有的id列表
     */
    public List<Long> selectAllIds() {
        return etlTableRepository.selectAllIds();
    }


    /**
     * @param tableIds
     * @return 批量创建excel
     */
    public List<File> createExcels(String tableIds) {
        String[] tableIdsArray = tableIds.split(",");
        List<File> fileList = new ArrayList<>();
        File file = null;
        for (String idStr : tableIdsArray) {
            Long tableId = Long.parseLong(idStr);
            file = createOneExcel(tableId);
            fileList.add(file);
        }
        return fileList;
    }


    /**
     * @param tableId 根据数据表id创建excel
     * @return 批量创建excel
     */
    public File createOneExcel(Long tableId) {
        if (tableId == null) {
            return null;
        }
        EtlTable etlTable = etlTableRepository.getOne(tableId);
        if (etlTable == null) {
            return null;
        }
        List<EtlTableConfig> etlTableConfigList = etlService.findByEtlTableId(tableId);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(etlTable.getTableDesc());//以中文名称命名excel文件
        HSSFRow row = sheet.createRow((0));
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCell cell;
        EtlTableConfig etlTableConfig;
        String[] dataList;
        CellRangeAddressList regions;
        DVConstraint constraint;
        HSSFDataValidation dataValidation;
        String referenceTable;
        String referenceColName;
        for (int i = 0; i < etlTableConfigList.size(); i++) {
            etlTableConfig = etlTableConfigList.get(i);
            cell = row.createCell(i);
            cell.setCellValue(etlTableConfig.getBaseColDesc());
            cell.setCellStyle(style);
            int firstCol = etlTableConfig.getSortNo().intValue();
            referenceTable = etlTableConfig.getReferenceTable();
            referenceColName = etlTableConfig.getReferenceColName();
            if (referenceTable != null && etlTableConfig.getReferenceColName() != null) {
                dataList = queryExcelDataList(referenceTable, referenceColName);
                regions = new CellRangeAddressList(1, 100, firstCol - 1, firstCol - 1);
                //创建下拉列表数据
                constraint = DVConstraint.createExplicitListConstraint(dataList);
                //绑定
                dataValidation = new HSSFDataValidation(regions, constraint);
                sheet.addValidationData(dataValidation);
            }
        }
        File directory = new File("d:/docs/");
        File file = null;
        try {
            //如果文件夹不存在 创建文件夹
            if (!directory.exists()) {
                boolean res = directory.mkdir();
                if (!res) {
                    return null;//创建失败则返回null
                }
            }
            String filePath = directory.getPath() + "/" + etlTable.getTableDesc() + ".xls";
            file = new File(filePath);
            //如果文件已经存在 先删除文件再创建文件
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @return 查询所有的ETL表。形成树状变为ztree的形式
     */
    public List<EtlTableTree> findEtlTableTree() {
        return etlTableTreeRepository.findAll();
    }


    /**
     * 向基础数据表中导入数据
     *
     * @param filePath
     * @param className
     * @param etlTableConfigList
     * @throws Exception
     */
    @Transactional
    public int[] importData(EtlTable etlTable, String filePath, String className, List<EtlTableConfig> etlTableConfigList) throws Exception {

        int importSize[] = importService.importDataFromExcel(etlTable, filePath, className, 1, etlTableConfigList);
        //数据要导入到基础表中
        return importSize;
    }


    /**
     * 从基础表转到业务表中
     */
    @Transactional
    public void transAndLoad(String baseTableName, List<String> resourceCols, String serviceTableName, List<String> targetCols) {

    }


    /**
     * @param file
     * @param request
     * @return 上传文件到文件服务器，若上传成功则将文件路径等基本信息保存在数据库
     */
    @Transactional
    public String uploadAndSave(MultipartFile file, Long tableId, HttpServletRequest request) {
        List<String> resultList = new ArrayList<>();//返回的结果
        String contextPath = SessionUtil.getContextPath(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String dirStr = "docs\\etlMedia\\" + timeStr;//ETL多媒体存放的文件夹，加上时间戳来唯一标识该时间上传的所有的文件
        String realDir = contextPath + dirStr;//绝对目录路径
        if (!UploadUtil.createDirectory(realDir)) {//目录创建失败则返回null
            System.out.println("--------EtlMediaFile----目录创建失败----");
            return null;
        }
        String fileName = file.getOriginalFilename();//文件名
        String filePath = realDir + "\\" + fileName;//绝对文件路径
        String type = file.getContentType();//获取文件类型
        Long mediaTypeId = 1L;//多媒体类型默认为文档
        if (type.contains("image")) {
            mediaTypeId = 2L;//多媒体类型 图片
        } else if (type.contains("video")) {
            mediaTypeId = 3L;//多媒体类型 视频
        }
        Boolean result = UploadUtil.uploadFiles(file, filePath);//上传文件到ftp
        if (result) {
            //如果上传到ftp成功，就保存多媒体信息到数据库
            EtlMedia etlMedia = new EtlMedia();
            etlMedia.setFileName(fileName);
            etlMedia.setFileSize(file.getSize());
            etlMedia.setFileRelativeUrl(dirStr + "\\" + fileName);//设置文件的相对路径，"docs\\etlMedia"+文件名
            etlMedia.setFileAbsoluteUrl(filePath);//设置文件的绝对路径，精确到盘符，如："E:\bmis0628\....\webapp"+"docs\\etlMedia"+文件名
            //  etlMedia.setMediaType(mediaTypeRepository.getOne(mediaTypeId));
            EtlTable etlTable = etlTableRepository.getOne(tableId);
            etlMedia.setEtlTable(etlTable);
            etlMedia.setStatus("1");//默认设置为1,为已审核状态
            EtlMedia etlMedia1 = etlMediaRepository.save(etlMedia);
            if (etlMedia1 != null) {
                System.out.println("--------EtlMediaFile----上传成功 保存成功----" + resultList);
                return filePath;
            } else {
                System.out.println("--------EtlMediaFile----上传成功 保存失败----" + resultList);
                return null;
            }
        } else {
            System.out.println("--------EtlMediaFile----上传失败----" + filePath);
            return null;
        }
    }


    /**
     * @param etlTableConfigList etl表配置信息
     * @return
     */
    public String getCreateTableContent(List<EtlTableConfig> etlTableConfigList) {

        StringBuilder sb = new StringBuilder();
        sb.append("id int8 NOT NULL,");
        Iterator<EtlTableConfig> it = etlTableConfigList.iterator();
        while (it.hasNext()) {
            EtlTableConfig config = it.next();
            log.info("sortNo----------getCreateTableContent----------" + config.getSortNo());
            sb.append(config.getBaseColName() + " ");
            sb.append(config.getDataType() + "(" + config.getLength() + ")");
            boolean isNull = config.getIsNull().equals("0");
            sb.append(isNull ? "" : " not null");
            sb.append(",");
        }
        String content = sb.toString();
        content = content.substring(0, content.length() - 1);
        log.info("--------------------" + content);
        return content;

    }


    /**
     * @param etlTableConfigList
     * @return
     */
    public String getInsertDataCols(List<EtlTableConfig> etlTableConfigList) {
        StringBuilder sb = new StringBuilder();
        List<String> colNameList = new ArrayList<>();
        sb.append("(id,");
        for (EtlTableConfig config : etlTableConfigList) {
            log.info(config.getBaseColName() + "----------getInsertDataCols----------" + config.getSortNo());
            if (config.getReferenceType().equals("1")) {
                colNameList.add(config.getServiceColName());
            } else {
                colNameList.add(config.getServiceColName());
            }
        }
        sb.append(String.join(",", colNameList));
        sb.append(")");
        String content = sb.toString();
        log.info("--------------------" + content);
        return content;
    }

    /**
     * @param etlTableConfigList
     * @return
     */
    public String getSelectDataCols(List<EtlTableConfig> etlTableConfigList) {
        StringBuilder sb = new StringBuilder();
        String seqName = "seq_person";
        List<String> colNameList = new ArrayList<>();
        sb.append("nextval('" + seqName + "'),");
        for (EtlTableConfig config : etlTableConfigList) {
            log.info(config.getBaseColName() + "----------getSelectDataCols----------" + config.getSortNo());
            //如果不参考  直接去 数据表字段
            switch (config.getReferenceType()) {
                case "0": {
                    String dateStr = config.getServiceColName();
                    if (config.getClassType().endsWith("Date")) {
                        dateStr = "to_date(" + config.getServiceColName() + ",'yyyy-MM-dd')";
                    }
                    colNameList.add(dateStr);
                }
                break;
                case "1": {
                    colNameList.add(config.getReferenceTable() + ".id");
                }
                break;
                default:
                    colNameList.add(config.getServiceColName());
                    break;
            }


            //如果参考静态值列表  根据静态值列表生成case when语句


            //如果参考动态值列表  根据关系 写出join语句


        }
        sb.append(String.join(",", colNameList));
        String content = sb.toString();
        log.info("--------------------" + content);
        return content;
    }


    /**
     * @param etlTableConfigList
     * @return
     */
    public String getJoinCondition(EtlTable etlTable, List<EtlTableConfig> etlTableConfigList) {
        StringBuilder sb = new StringBuilder();
        List<String> joinList = new ArrayList<>();
        for (EtlTableConfig config : etlTableConfigList) {
            log.info(config.getBaseColName() + "----------getJoinCondition----------" + config.getSortNo());
            if (config.getReferenceType().equals("1")) {
                String referenceTable = config.getReferenceTable();
                joinList.add(" join " + referenceTable + " on " + etlTable.getBaseTableName() + "." + config.getBaseColName() + "=" + referenceTable + "." + config.getReferenceColName());
            }
        }
        sb.append(String.join(" ", joinList));
        String content = sb.toString();
        log.info("--------------------" + content);
        return content;
    }


    /**
     * @param tableName
     * @param colName
     * @return 查询excel下拉列表
     */
    public String[] queryExcelDataList(String tableName, String colName) {
        String sql = "select distinct " + colName + " from " + tableName;
        List<String> stringList = jdbcTemplate.queryForList(sql, String.class);
        int size = stringList.size();
        return stringList.toArray(new String[size]);//使用了第二种接口，返回值和参数均为结果;
    }
}