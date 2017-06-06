$(function () {
    DisPatchFormWizard.init();

    $('select').select2({theme: "bootstrap"});


    /*
     $('#addUnitForm')
     .bootstrapValidator({
     message: '该值无效 ',
     fields: {
     unitNo: {
     message: '单位编号无效',
     validators: {
     notEmpty: {
     message: '单位编号不能为空!'
     },
     stringLength: {
     min: 3,
     max: 20,
     message: '单位编号长度为3到20个字符'
     }
     }
     },
     description: {
     message: '单位名称无效',
     validators: {
     notEmpty: {
     message: '单位名称不能为空!'
     },
     stringLength: {
     min: 2,
     max: 20,
     message: '单位名称长度为2到20个字符'
     }
     }
     },
     "status": {
     message: '单位状态无效',
     validators: {
     notEmpty: {
     message: '单位状态不能为空!'
     }
     }
     }
     }
     })
     .on('success.form.bv', function (e) {
     // Prevent form submission
     e.preventDefault();
     createUnit();
     });
     */


});
var selectedId = [];
function generateReport() {
    var ids = selectedId.join(",");
    if (!ids) {
        showMessageBoxCenter("danger", "center", "请选择要操作的记录!");
        return
    }
    $("#modal_div").load("/workOrderReportCart/loadDetailList");
    $("#cart_modal").modal("show")
}
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
    var confirm = window.confirm("确认将该报修信息移出报修车么？");
    if (confirm) {
        var url = "/workOrderReportCart/delCart";
        $.post(url, {id: id}, function (data) {
            $("#tr" + id).remove();
            showMessageBox("info", "已将报修信息移出报修车")
        })
    }
}
function checkAll(obj) {
    $("#account input[type='checkbox']").prop("checked", $(obj).prop("checked"))
}
$("input[name^='selUnit']").on("click", function () {

    alert($(this).attr("name"));
});


function selectUnit(name) {
    var unitId = $("#" + name).val();
    var detailId = name.substring(7);
    //获取当前设备分类对应的所有维修单位
    updateDetailUnit(detailId, unitId);
}


/**
 *
 * @param select 查询所有的外委单位
 * @param orderId
 */
function loadUnit(select) {
    var url = "/units/findByStatus/1";
    $.getJSON(url, function (data) {
        $("#" + select).empty();
        var html = "";
        for (var x in data) {
            html += "<option value='" + data[x].id + "'>" + data[x].description + "</option>"
        }
        $("#" + select).html(html);
    });
}


/**
 *
 * @param detailId 明细ID
 * @param unitId 维修单位ID
 */
function updateDetailUnit(detailId, unitId) {

    var url = "/workOrderDispatch/updateDetailUnit";

    if (detailId && unitId) {
        $.post(url, {detailId: detailId, unitId: unitId}, function (data) {
            showMessageBox("info", "维修单位设置成功");
        })
    } else {


    }


}


/**
 *
 * @param cid  设备分类ID
 * 关联单位
 */

var currentCid = null;
var workOrderId = null;
function linkUnit(wid, cid) {
    //弹出模态框  选中一个单位 点击确定
    currentCid = cid;
    var url = "equipmentsClassification/popUnits/" + cid;
    //var url = "/equipmentsClassification/loadSelectUnitPage/" + cid;
    $("#unitBody").load(url);
    $("#link_unit_modal").modal("show");
    workOrderId = wid;
    //提示关联单位成功  并将加入到对应的列表中

}


/**
 *
 * @param cid  设备分类ID
 * 添加并且关联单位
 */

var eqClassId = null;
function addLinkUnit(wid, cid) {
    //弹出模态框  新增一个单位 点击确定
    eqClassId = cid;
    workOrderId = wid;
    console.log("eqClassId---------------" + eqClassId);
    $("#add_link_unit_modal").modal("show");
    //提示新增并且关联单位成功  并将加入到对应的列表中
}


function confirmLinkUnit() {
    var ids = "";
    $("#unitsNotInEqClass input[type='checkbox']").each(function (i) {
        if ($(this).is(":checked") && !isNaN($(this).val())) {
            ids += $(this).val() + ",";
        }
    });
    ids = ids.replace(/\ +/g, "").replace(/[\r\n]/g, "");
    if (!ids) {
        showMessageBox("danger", "请选择外委单位！");

    } else {
        //加入
        $("#link_unit_modal").modal("hide");
        var url = "/equipmentsClassification/addU2c";
        console.log("currentCid-----------"+currentCid);
        console.log("ids-----------"+ids);
        console.log("workOrderId-----------"+workOrderId);
        // 提示操作成功或失败
        $.post(url, {cid: currentCid, ids: ids, workOrderId: workOrderId}, function (data) {
            if (data) {
                $("#selUnit" + workOrderId).empty();
                $("#selUnit" + workOrderId).append("<option value=''>请选择外委单位</option>");
                for (var x in data) {
                    if (data[x]["id"] && data[x]["description"]) {
                        $("#selUnit" + workOrderId).append("<option value='" + data[x]["id"] + "'>" + data[x]["description"] + "</option>");
                    }
                }
                //清空select 重新载入外委单位放入
                showMessageBox("info", "设备分类关联外委单位成功！")
            } else {
                showMessageBox("danger", "设备分类关联外委单位失败！")
            }
        });
    }
}


function createUnit() {
    var object = {
        unitNo: $("#unitNo").val(),
        description: $("#description").val(),
        linkman: $("#linkman").val(),
        telephone: $("#telephone").val(),
        eqClassId: eqClassId
    };

    var url = "/units/saveLink";
    $.ajax({
            type: "post",
            url: url,
            data: object,
            dataType: "json",
            success: function (data) {
                $("#add_link_unit_modal").modal("hide");
                showMessageBox("info", "外委单位信息添加成功");
                //保存完之后将当前添加的信息添加到下拉列表中
                $("#selUnit" + workOrderId).empty();
                $("#selUnit" + workOrderId).append("<option value=''>请选择外委单位</option>");
                for (var x in data) {
                    if (data[x].id && data[x].description) {
                        $("#selUnit" + workOrderId).append("<option value='" + data[x].id + "'>" + data[x].description + "</option>");
                    }
                }
            }, error: function (data) {
                showMessageBox("danger", "外委单位信息添加失败");
            }
        }
    );
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
