var listTab = $('#myTab li:eq(0) a');

$(function () {

    //初始化配置信息
    //主对象信息
    mainObject = "eqBatchUpdateBill";
    //列表id
    dataTableName = "#batchListTable";
    //导出文档配置信息
    docName = "设备更新申请单信息";
    //查询模型
    searchModel = [{"param": "locName", "paramDesc": " 位置"}, {"param": "eqClass", "paramDesc": "设备分类"}];

    //配置动态列表

    initBootGridMenu(dataTableName, null);
    initSelect.call();
    // //初始化查询所有的
    // ids = findAllRecordId();
    // console.log("ids------" + JSON.stringify(ids));
    // selectedIds = ids;
});


