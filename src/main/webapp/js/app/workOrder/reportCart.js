jQuery(document).ready(function () {
    FormWizard.init()
});
var selectedId = [];
function confirmGenerate() {
    $("#cart_modal").modal("hide");
    var ids = selectedId.join(",");
    var url = "/workOrderReport/generateReport";
    $.post(url, {ids: ids}, function (data) {
        showMessageBox("info", "报修单已生成")
    })
}
function save() {
    var orderReportList = [];
    $("input[id^='orderDesc']").each(function () {
        var name = $(this).attr("id");
        var id = name.substring(9, name.length);
        var orderDesc = $(this).val();
        var obj = {id: id, orderDesc: orderDesc};
        if (id && orderDesc) {
            orderReportList.push(obj)
        } else {
            orderReportList.push(null)
        }
    });
    var result = true;
    for (var x in orderReportList) {
        var obj = orderReportList[x];
        var url = "/workOrderReport/saveOrderDesc";
        if (!obj) {
            showMessageBoxCenter("danger", "center", "设备故障描述不能为空!");
            result = false;
            break
        }
        $.post(url, {id: obj.id, orderDesc: obj.orderDesc}, function (data) {
        })
    }
    if (result) {
        $("#resultListDiv").load("/workOrderReport/showUpdated");
        showMessageBox("info", "")
    }
}
function delCart(id) {
    bootbox.confirm({
        message: "确认将该报修信息移出报修车么?",
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

                var url = "/workOrderReportCart/delCart";
                $.post(url, {id: id}, function (data) {
                    $("#tr" + id).remove();
                    showMessageBox("info", "已将报修信息移出报修车");
                });
            }
        }
    });
}
function checkAll(obj) {
    $("#account input[type='checkbox']").prop("checked", $(obj).prop("checked"))
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
 *
 * @param eid
 */
function showFixList(eid) {
    //先判断是否eid有效
    if (eid) {
        var url = "/equipment/showFixList";
        $("#eqFixBody").load(url, {eid: eid}, function (data) {
            $("#eqFixModal").modal("show");
        });

    } else {
        showMessageBox("danger", "该报修单未关联设备信息！");
    }

    //异步加载该设备维修记录
}

/**
 *
 * @param lid
 * @param cid
 * 显示本站同类设备的报修历史信息
 */
function showClassFixList(lid, cid) {
    //先判断是否eid有效
    if (lid && cid) {
        var url = "/equipment/showClassFixList";
        var data = {lid: lid, cid: cid};
        $("#locClassFixBody").load(url, data, function (data) {
            $("#locClassFixModal").modal("show");
        });
    } else {
        showMessageBox("danger", "该报修单设备位置或者设备分类有误！");
    }

}


