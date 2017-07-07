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
    var tableConfig =
        {
            selection: true,
            multiSelect: true,
            sorting: true,
            rowSelect: true,
            keepSelection: true,
            navigation: 0,
            rowCount: [8]
        };
    $(dataTableName).bootgrid(tableConfig);
    initSelect.call();
});


/**
 *  申请通过
 */
function approve() {
    var ids = $(dataTableName).bootgrid("getSelectedRows") + ",";
    console.log("-------------------" + ids);
    var url = "/eqBatchUpdateBill/approve";
    var data = {
        ids: ids
    };
    $.post(url, data, function (data) {
        if (data.result) {
            showMessageBox("info", data.resultDesc);
            $(dataTableName).bootgrid("reload");
        } else {
            showMessageBox("danger", data.resultDesc);
        }
    })
}





