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
        "location": {
            message: '位置编号无效',
            validators: {
                notEmpty: {
                    message: '位置编号不能为空!'
                }
                ,
                stringLength: {
                    min: 2,
                    max: 20,
                    message: '位置编号长度为2到20个字符'
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
        //firstLoad(zNodes[0]);


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


        console.log("save---------------------");
    })
});
var flag = false;


function add() {
    // var parent = addNode();
    var url = mainObject + "/create/" + getSelectedNode().id;
    $.getJSON(url, function (data) {
        vdm.$set(mainObject, data);
    })
    setFormReadStatus(formName, false, "location");
    $("#locNameDiv").hide();
}


var reportId;

function report(id) {
    var status = "0";
    var path = "/equipment/findById/" + id;
    $.getJSON(path, function (data) {
        status = data["status"]
    });
    var curl = "/workOrderReportCart/loadReportedEqPage/" + id;
    if (status == "0") {
        $("#eqList").load(curl, function (data) {
            $("#show_eq_modal").modal("show");
            eqId = id;
            reportId = id;
        })
    } else {
        equipReport(id);
    }
}

function equipReport(id) {
    var url = "/workOrderReportCart/add2Cart";
    $.post(url, {equipmentId: id}, function (data) {
        var size = $("#reportOrderSize").html();
        if (!size) {
            size = 0
        }
        $("#reportOrderSize").html(parseInt(size) + 1);
        showMessageBox("info", "已将设备报修加入到维修车!")
    })
}


/**
 *  删除位置信息
 */
function del() {
    var zTree = $.fn.zTree.getZTreeObj("tree");
    var selectedNode = zTree.getSelectedNodes()[0];
    var id = selectedNode.id;
    var url = "/location/delete/" + id;

    if (id) {
        bootbox.confirm({
            message: "确定要删除该记录么？?",
            buttons: {
                confirm: {
                    label: '是',
                    className: 'btn-success'
                },
                cancel: {
                    label: '否',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    $.ajax({
                        type: "GET",
                        url: url,
                        success: function (msg) {
                            if (msg.result) {
                                showMessageBox("info", msg["resultDesc"]);
                                var zTree = $.fn.zTree.getZTreeObj("tree");
                                zTree.removeNode(zTree.getSelectedNodes()[0]);
                                zTree.selectNode(zTree.getNodeByParam("id", 1));
                            }
                        },
                        error: function (msg) {
                            showMessageBox("danger", msg["resultDesc"]);
                        }
                    });
                }
            }
        });
    }
}

/**
 * 位置保修
 */
function batchUpdateReport() {
    var location = getSelectedNode().location;
    var locationId = getSelectedNode().id;
    var status = "0";
    var locType = "";
    var path = "/location/findById/" + locationId;
    $.getJSON(path, function (data) {
        status = data["status"];
        locType = data["locationType"];
    });
    if (!location) {
        showMessageBox("danger", "请先选中位置再进行设备更新操作!");
        return
    }
    var url = "/commonData/findVEqClass";
    $.getJSON(url, function (data) {
        eqClasses = data;
    });

    console.log("---------------------" + JSON.stringify(eqClasses));
    //新建一个数据模型
    //初始化请求设备分类
    reportModel = new Vue({
        el: "#locReportForm",
        data: {
            eqClasses: eqClasses

        }
    });
    console.log("here-------------------");
    $("#rptLoc").val(getSelectedNode().name);
    $("#loc_modal").modal("show");
}

/**
 *  已经报修提示重复报修 选择继续
 */
function continueLocReport() {
    $("#show_loc_modal").modal("hide");
    //再次报修时  将原来的输入清空
    $("#orderDesc").val("");
    $("#rptLoc").val(getSelectedNode().name);
    $("#loc_modal").modal("show");
}

/**
 * 加入位置报修
 */
function applyReport() {
    var nodeId = getSelectedNodeId();
    var applicant = $("#applicant").val();
    var applyDep = $("#applyDep").val();
    var applyDate = $("#applyDate").val();
    var purpose = $("#purpose").val();
    var approver = $("#approver").val();
    var handler = $("#handler").val();
    var receiver = $("#receiver").val();


    var eqClassId = $("#equipmentsClassification_id").val();

    if (!nodeId) {
        showMessageBox("danger", "请选中设备更新位置!");
        return;
    }

    if (!eqClassId) {
        showMessageBox("danger", "请选择要更新的设备类型!");
        $("#equipmentsClassification_id").focus();
        $("#equipmentsClassification_id").css("border", "dashed 1px red");
        return;
    }
    if (!applicant) {
        showMessageBox("danger", "申请人不能为空!");
        $("#applicant").focus();
        $("#applicant").css("border", "dashed 1px red");
        return
    }
    if (!applyDep) {
        showMessageBox("danger", "申请部门不能为空!");
        $("#applyDep").focus();
        $("#applyDep").css("border", "dashed 1px red");
        return
    }
    if (!purpose) {
        showMessageBox("danger", "申请用途不能为空!");
        $("#purpose").focus();
        $("#purpose").css("border", "dashed 1px red");
        return
    }
    if (!applyDate) {
        showMessageBox("danger", "申请日期不能为空!");
        $("#applyDate").focus();
        $("#applyDate").css("border", "dashed 1px red");
        return
    }
    if (!approver) {
        showMessageBox("danger", "批准人人不能为空!");
        $("#approver").focus();
        $("#approver").css("border", "dashed 1px red");
        return
    }
    if (!handler) {
        showMessageBox("danger", "经办人不能为空!");
        $("#handler").focus();
        $("#handler").css("border", "dashed 1px red");
        return
    }
    if (!receiver) {
        showMessageBox("danger", " 接收人不能为空!");
        $("#receiver").focus();
        $("#receiver").css("border", "dashed 1px red");
        return
    }

    $("#locReportForm #locationId").val(nodeId);


    var obj = getFormJsonData("locReportForm");
    obj["location.id"]= nodeId;
    var objJson = JSON.parse(obj);
    var url = "eqBatchUpdate/save";


    // var object = {
    //     applicant: applicant,
    //     loc: nodeId,
    //     eqClassId: eqClassId,
    //     applyDep: applyDep,
    //     applyDate: applyDate,
    //     purpose: purpose,
    //     approver: approver,
    //     handler: handler,
    //     receiver: receiver
    // };

    console.log("url------------------" + url);

    console.log("object------------------" + JSON.stringify(objJson));
    $.post(url, objJson, function (data) {
        $("#loc_modal").modal("hide");
        if (data.result) {
            showMessageBox("info", "设备更新申请已提交!")
        } else {
            showMessageBox("danger", "设备更新申请提交失败!")
        }
    });

}

/**
 *
 * @param data
 * 首次加载函数 在form中显示第一条记录内容
 */

/*
 function firstLoad(data) {
 if (data.length > 0) {
 $("#form #lid").val(data.id);
 $("#form #location").val(data.location);
 $("#form #description").val(data.name);
 $("#form #superior").val(data.superior);
 //$("#parent_id").val(null).attr("readonly", "readonly");

 }
 }
 */


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