package com.subway.controller.etl;


import com.subway.controller.common.BaseController;
import com.subway.domain.app.MyPage;
import com.subway.domain.etl.EtlTable;
import com.subway.domain.etl.EtlTableConfig;
import com.subway.object.ReturnObject;
import com.subway.service.etl.EtlSearchService;
import com.subway.service.etl.EtlService;
import com.subway.service.etl.EtlTableService;
import com.subway.utils.PageUtils;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by huangbin on 2017-08-16 09:53:27
 */
@Controller
@RequestMapping("/etl")
public class EtlController extends BaseController {
    @Autowired
    EtlService etlService;

    @Autowired
    EtlSearchService etlSearchService;

    @Autowired
    EtlTableService etlTableService;

    String objectName = "元数据表属性信息";


    @RequestMapping(value = "/list")
    public String list(HttpSession session, ModelMap modelMap) {
        //加载查询菜单
        return super.list(session, modelMap);
    }
    /**
     * @param tableIds
     * @return 根据tableIds导出多个excel模板
     * @throws WriteException
     * @throws IOException
     */
    @RequestMapping(value = "/createExcels", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject createExcels(@RequestParam("tableIds") String tableIds) throws WriteException, IOException {
        List<File> files = etlTableService.createExcels(tableIds);
        return getCommonDataService().getReturnType(!files.isEmpty(), files.size() + "个文件创建成功！", "文件创建失败！");
    }

    /**
     * @param file
     * @param tableId 表id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadAndSave", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject uploadAndSave(@RequestParam("file") MultipartFile file, @RequestParam("tableId") Long tableId, HttpServletRequest request) throws Exception {
        String filePath = etlTableService.uploadAndSave(file, tableId, request);
        //文件路径
        //业务表名称
        int[] importSize = {};
        EtlTable etlTable = etlTableService.findById(tableId);
        List<EtlTableConfig> etlTableConfigList = etlService.findByEtlTableId(tableId);
        //并将数据导入到基础表中
        //判断基础表是否存在  如果不存在  创建基础表
        boolean exist = etlService.isTableExist(etlTable.getBaseTableName());
        log.info("exist base table-----------" + exist);
        etlService.dropBaseTable("sde", etlTable);
        etlService.createBaseTable("sde", etlTable, etlTableConfigList);
        //根据基础表和业务表配置关系  将外键数据导入到业务表中
        try {
            importSize = etlTableService.importData(etlTable, filePath, etlTable.getDomainName(), etlTableConfigList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将数据导入到基础表当中
        log.info("表已经存在 直接导入数据");
        etlService.importServiceDataSql("sde", etlTable, etlTableConfigList);
        return getCommonDataService().getReturnType(importSize.length != 0, "条数据导入成功", "数据导入失败");
    }

    /**
     * 分页查询
     *
     * @param current      当前页
     * @param rowCount     每页条数
     * @param searchPhrase 查询关键字
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public MyPage data(HttpServletRequest request,
                       @RequestParam(value = "current", defaultValue = "0") int current,
                       @RequestParam(value = "rowCount", defaultValue = "10") Long rowCount,
                       @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Pageable pageable = new PageRequest(current - 1, rowCount.intValue(), super.getSort(parameterMap));
        return new PageUtils().searchBySortService(etlSearchService, searchPhrase, 1, current, rowCount, pageable);
    }


    /**
     * @param etlTableConfig
     * @return 保存元数据表信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject save(EtlTableConfig etlTableConfig) {
        etlService.save(etlTableConfig);
        return super.save(objectName, etlTableConfig);
    }

    /**
     * @return 查找所有的元数据表信息信息
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public List<EtlTableConfig> findAll() {
        return etlService.findAll();
    }

    /**
     * @param id 对象id
     * @return 根据id查询元数据表信息信息
     */
    @RequestMapping("/findById/{id}")
    @ResponseBody
    public EtlTableConfig findById(@PathVariable("id") Long id) {
        return etlService.findById(id);
    }

    /**
     * @param id
     * @return 根据id删除元数据表信息信息
     */
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public ReturnObject delete(@PathVariable("id") Long id) {
        return etlService.delete(id);
    }

    /**
     * @return 选择所有的id列表
     */
    @ResponseBody
    @RequestMapping(value = "/selectAllIds", method = RequestMethod.GET)
    public List<Long> selectAllIds() {
        return etlService.selectAllIds();
    }

    /**
     * @param etlTableId
     * @return 根据etlTableId查询该表的所有属性信息EtlTableConfig
     */
    @ResponseBody
    @RequestMapping(value = "/findByEtlTableId/{etlTableId}", method = RequestMethod.GET)
    public List<EtlTableConfig> findByEtlTableId(@PathVariable("etlTableId") Long etlTableId) {
        return etlService.findByEtlTableId(etlTableId);
    }
}


