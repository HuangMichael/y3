var zTree;
var demoIframe;
var reportModel = null;
var eqClasses = [];
var setting = {
    check: {enable: false},
    view: {dblClickExpand: false, showLine: true, selectedMulti: false},
    data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: ""}},
    callback: {
        onClick: function (event, treeId, treeNode) {
            var locName = findLocNameById(treeNode.id);
            console.log("locName---------------" + locName);
            vdm.$set("locName", locName);

            $("#locName").val(locName);
            var node = findObjById("location", treeNode.id);
            vdm.$set("location", node);

            setFormReadStatus("#detailForm", true);
            loadEqList(treeNode.id);
            return true;
        }
    }
};
var zNodes = [];
var validationConfig = {
    message: '该值无效 ',
    fields: {
        "applicant": {
            message: '申请人无效',
            validators: {
                notEmpty: {
                    message: '申请人不能为空!'
                }
                ,
                stringLength: {
                    min: 2,
                    max: 20,
                    message: '申请人长度为2到20个字符'
                }
            }
        }
        ,
        "description": {
            message: '位置描述无效',
            validators: {
                notEmpty: {
                    message: '位置描述不能为空!'
                }
                ,
                stringLength: {
                    min: 2,
                    max: 20,
                    message: '位置描述长度为2到20个字符'
                }
            }
        }
    }
};


$(document).ready(function () {
    var url = "/location/findTree";
    var pid = 0;
    $.ajaxSettings.async = false;
    $.getJSON(url, function (data) {
        for (var x = 0; x < data.length; x++) {
            zNodes[x] = {
                id: data[x][0],
                location: data[x][1],
                name: data[x][2],
                superior: data[x][3],
                pId: (data[x][4]) ? (data[x][4]) : 0,
                open: false,
                isParent: pid
            };
        }

        var t = $("#tree");
        t = $.fn.zTree.init(t, setting, zNodes);
        demoIframe = $("#testIframe");
        demoIframe.bind("load", loadReady);
        zTree = $.fn.zTree.getZTreeObj("tree");
        zTree.selectNode(zTree.getNodeByParam("id", zNodes[0]));
        afterClick.call(zNodes[0]["id"]);
    });

    function loadReady() {
        var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
            htmlH = demoIframe.contents().find("html").get(0).scrollHeight, maxH = Math.max(bodyH, htmlH),
            minH = Math.min(bodyH, htmlH), h = demoIframe.height() >= maxH ? minH : maxH;
        if (h < 530) {
            h = 530
        }
        demoIframe.height(h)
    }


    dataTableName = "#eqDataTable";
    formName = "#detailForm";
    mainObject = "location";
    docName = "设备信息";
    exportObject = "equipment";
    validateForm(validationConfig);

    vdm = new Vue({
        el: formName,
        data: {
            location: null,
            locName: ""

        }
    });
    initSelect();


    $("#saveBtn").on("click", function () {

        alert(123123);

    });
});

/**
 *
 * @param id 点击树节点触发
 */
function afterClick(id) {
    fillForm(id);
    loadEqList(id);
}


/**
 *
 * @param locationId
 */
function loadEqList(locationId) {
    //载入页面并刷新
    var url = "location/detail/" + locationId;
    $("#tab_1_1").load(url, function (data) {

    });
    $("#eqDataTable").bootgrid({
        rowCount: [8],
        formatters: {
            "report": function (column, row) {
                return '<a class="btn btn-default btn-xs"  onclick="report(' + row.id + ')" title="报修" ><i class="glyphicon glyphicon-wrench"></i></a>';
            }
        }
    });


}


/**
 *根据位置id查询位置详细名称
 * @param id
 * @return {*}
 * */
function findLocNameById(id) {
    $.ajaxSettings.async = false;
    var object = "";
    var url = "/location/findLocNameById/" + id;
    $.getJSON(url, function (data) {
        object = data;
    });
    return object.locName;
}


/**
 * 导入位置信息
 */
function importLoc() {
    //弹出框 输入设备分类
    var lid = getSelectedNodeId();
    $("#importLocModal").modal("show");
    var split = "\n"; //使用换行符分割
    $("#confitmBtna").on("click", function () {
        var locStr = $("#locStrField").val().trim();
        var url = "/location/importLoc";
        var data = {
            lid: lid,
            locStr: locStr,
            split: split
        };
        $.post(url, data, function (data) {
            if (data.result) {
                showMessageBox("info", data["resultDesc"]);
            } else {
                showMessageBox("danger", data["resultDesc"]);
            }
            $("#importLocModal").modal("hide");
        });
    });
}


/**
 * 更新设备位置车站和线路信息
 */
function sysnLoc() {
    //弹出框 输入设备分类
    var lid = getSelectedNodeId();
    var url = "/location/sysnLoc";
    var data = {
        lid: lid
    };
    $.post(url, data, function (data) {
        if (data.result) {
            showMessageBox("info", data["resultDesc"]);
        } else {
            showMessageBox("danger", data["resultDesc"]);
        }
        $("#importLocModal").modal("hide");
    });
}


function batchUpdateReport() {
    var lid = getSelectedNodeId();
    console.log("------------------" + lid);
    $("#report_modal :input").removeAttr("readonly");
    $("#report_modal").modal("show");


}


/**
 *
 */
function saveBatchBill() {

    var obj = getFormJsonData("batchUpdateForm");

    console.log("obj-----------"+obj);
    $("#report_modal").modal("hide");
}



