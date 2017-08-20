/**
 * Created by Administrator on 2016/7/22.
 */


var expiredTab = $('#myTab li:eq(1) a');


var selectedId = null;
$(document).ready(function () {

    docName = "维修单信息";
    mainObject = "workOrderFix";
    //导出必须配置的两个量
    dataTableName = "#fixListTable0";

    var dataTableName1 = "#expiredOrdersList";


    var url_location = "/commonData/findMyLoc";
    $.getJSON(url_location, function (data) {
        locs = data;
    });


    var url = "/commonData/findVEqClass";
    $.getJSON(url, function (data) {
        eqClasses = data;
    });


    initSelect();

    var searchVue = new Vue({
        el: "#searchBox",
        data: {
            locs: locs,
            eqClasses: eqClasses
        }

    });

    searchModel = [
        {"param": "location", "paramDesc": "位置编码"},
        {"param": "nodeState", "paramDesc": "工单状态"},
        {"param": "orderLineNo", "paramDesc": "跟踪号"},
        {"param": "orderDesc", "paramDesc": "故障描述"},
        {"param": "locName", "paramDesc": "设备位置"},
        {"param": "eqClass", "paramDesc": "设备分类"},
        {"param": "expired", "paramDesc": "是否超期"}

    ];
    initBootGrid(dataTableName);
    initBootGrid(dataTableName1);
    $("#expiredTip").html(getExpiredCount());

    //计算超期 并且没有完工的工单数量
    setInterval(function () {
        $("#expiredTip").fadeOut(1000).fadeIn(1000);
    }, 1000);


    $("#searchBtn").trigger("click", function () {


        console.log("-------search-------------------");
    });


    $(":checkbox").on("click", function () {
        if ($(this).prop("checked")) {
            $(this).siblings().attr("checked", false);
            $(this).attr("checked", true);
        }
    })

    $("#saveFixDesc").on("click", function () {
        var orderId = $("#orderId").val();
        var operationType = $("#operationType").val();
        var operationDesc = $("#operationDesc").val();
        var fixDesc = $("#fixDesc").val();

        if (!fixDesc) {
            $("#fixDesc").css("border", "red dashed 1px");
            showMessageBox("danger", " 请输入维修描述");
            return;
        }
        dealResultDetail(orderId, operationType, operationDesc, fixDesc);

    });
    $("#myTab a").on("click", function (e) {
        e.preventDefault();
        $(this).tab('show');
    });


    expiredTab.on("click", function () {
        var searchPhase = "已派工,,,,,已超期,";
        $(dataTableName1).bootgrid("setSearchPhrase", searchPhase).bootgrid("reload");
        //$("#expiredOrderSize").html(expiredCount);
    })


    $("#applyUpdate").on("click", function () {
        var orderId = $(dataTableName).bootgrid("getSelectedRows");
        $("#saveFixDesc").trigger("click");
        var workOrder = findByIdAndObjectName(orderId, "workOrderReportCart");
        console.log("workOrder--------------" + JSON.stringify(workOrder));
        //根据选择的获取对应的工单编号  根据工单编号获取设备信息

        $("#eId").val(workOrder.locations.id);
        $("#eqClassId").val(workOrder.equipmentsClassification.id);
        $("#loc_modal").modal("show");

    });
});


function dealResult(orderId, operationType, operationDesc) {
    $("#orderId").val(orderId);
    $("#operationType").val(operationType);
    $("#operationDesc").val(operationDesc);
    $("#fix_desc_modal").modal("show");
}


function dealResultDetail(orderId, operationType, operationDesc, fixDesc) {
    updateOrderStatus(orderId, operationType, operationDesc, fixDesc);
    $(dataTableName).bootgrid("reload");
    //更新超期
    $("#expiredTip").html(expiredCount);
}


/**
 *  调整维修期限
 * @param id
 */
function adjust(id) {
    var orderId = id;
    var url = "/workOrderFix/getCellingDate/" + orderId;
    $("#orderId").val(orderId);
    $.getJSON(url, function (data) {
        $("#fixAdjust0").val(transformDate(data));
        $("#fix_adjust_modal").modal("show");
    })
}


/**
 *
 * @param orderId
 * @param deadLine
 */
function confirmAdjust() {
    var url = "workOrderFix/updateDeadLine";
    var data = {
        orderId: $("#orderId").val(),
        deadLine: $("#fixAdjust1").val()
    };
    $.post(url, data, function (data) {
        if (data.result) {
            showMessageBox("info", data['resultDesc']);
        } else {
            showMessageBox("danger", data['resultDesc']);
        }
        $("#fix_adjust_modal").modal("hide");
        $('#fixListTable').bootgrid();
    });

}


function updateOrderStatus(orderId, operationType, operationDesc, fixDesc) {
    var url = "/workOrderFix/" + operationType;
    $.post(url, {fixId: orderId, fixDesc: fixDesc}, function (data) {
        $("#fix_desc_modal").modal("hide");
        $("#tr-" + orderId).html(operationDesc);
        $("#tr-" + orderId).html(fixDesc);
        (data.result) ? showMessageBox("info", data['resultDesc']) : showMessageBox("danger", data['resultDesc']);
    });
}


/**
 *
 * @param id 完工
 */
function finish() {
    var orderId = $(dataTableName).bootgrid("getSelectedRows")[0];
    if (!orderId) {
        showMessageBox("danger", "请选择一个要完工的工单！")
        return;
    }
    var operationType = "finishDetail";
    var operationDesc = "完工";
    dealResult(orderId, operationType, operationDesc);
}


/**
 *
 * @param id 暂停
 */
function pause() {
    var orderId = $(dataTableName).bootgrid("getSelectedRows")[0];
    if (!orderId) {
        showMessageBox("danger", "请选择一个要暂停的工单！")
        return;
    }
    var operationType = "pauseDetail";
    var operationDesc = "暂停";
    dealResult(orderId, operationType, operationDesc);
}

/**
 *
 * @param id 取消
 */
function abort() {
    var orderId = $(dataTableName).bootgrid("getSelectedRows")[0];
    if (!orderId) {
        showMessageBox("danger", "请选择一个要取消的工单！")
        return;
    }
    var operationType = "abortDetail";
    var operationDesc = "取消";
    dealResult(orderId, operationType, operationDesc);
}

/**
 *
 * @param eid 设备id
 * 根据设备ID信息查询设备详细信息  弹窗口显示
 */
function showEqDetailByEqId(eid) {
    var url = "/equipment/findById/" + eid;
    $.getJSON(url, function (data) {
        $("#eqNo").val(data.eqCode);
        $("#eqName").val(data.description);
        $("#location").val(data.vlocations.locName);
        $("#eqClass").val(data.equipmentsClassification.description);
        $("#eqModel").val(data.eqModel);
        $("#productFactory").val(data.productFactory);
        $("#eqInfoModal").modal("show");
    });
}


/**
 * 批量完工
 */
function finishOrderBatch() {
    var orderIds = $(dataTableName).bootgrid("getSelectedRows");
    if (orderIds.length == 0) {
        showMessageBox("danger", "请选择要完工的工单!");
        return;
    }
    var url = "/workOrderFix/finishBatch";
    var data =
        {
            orderIds: orderIds.join(",")
        }
    ;
    $.post(url, data, function (data) {
        if (data.result) {
            showMessageBox("info", data.resultDesc);
            complexSearch();
        } else {
            showMessageBox("danger", data.resultDesc);
        }

    });
}


/**
 * 加入位置报修
 */
function fillReport(eid, locId, eqClassId) {

    var applicant = $("#applicant").val();
    var applyDep = $("#applyDep").val();
    var applyDate = $("#applyDate").val();
    var purpose = $("#purpose").val();
    var approver = $("#approver").val();
    var handler = $("#handler").val();
    var receiver = $("#receiver").val();

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
    var obj = getFormJsonData("locReportForm");
    var objJson = JSON.parse(obj);
    var url = "eqBatchUpdate/save";
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
 * 批量完工更新
 */
function updateOrderBatch() {
    var orderId = $(dataTableName).bootgrid("getSelectedRows");
    // $("#saveFixDesc").trigger("click");
    var workOrder = findByIdAndObjectName(orderId, "workOrderReportCart");
    //根据选择的获取对应的工单编号  根据工单编号获取设备信息
    if (!workOrder.equipments) {
        showMessageBox("danger", "该维修单未关联设备信息,无法进行设备更新操作!");
        return;
    }
    $("#locId").val(workOrder.locations.id);
    $("#eqIds").val(workOrder.equipments.id + ",");
    $("#eqClassId").val(workOrder.equipmentsClassification.id);

    $("#loc_modal").modal("show");
}






