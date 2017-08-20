var zTree;
var demoIframe;
var reportModel = null;
var eqClasses = [];
var setting = {
    view: {dblClickExpand: false, showLine: true, selectedMulti: false},
    data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: ""}},
    callback: {
        onClick: function (event, treeId, treeNode) {
            var node = findObjById("location", treeNode.id);
            console.log("node----------------" + JSON.stringify(node));
            setLocForm(node);
            return true;
        }
    }
};
var zNodes = [];
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
                open: data[x][1].length < 4,
                isParent: pid,
                halfCheck: true
            };
        }

        var t = $("#tree");
        t = $.fn.zTree.init(t, setting, zNodes);
        demoIframe = $("#testIframe");
        demoIframe.bind("load", loadReady);
        zTree = $.fn.zTree.getZTreeObj("tree");
        zTree.selectNode(zTree.getNodeByParam("id", zNodes[0]));


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


    vdm = new Vue({
        el: "#detailForm",
        data: location


    });
});


/**
 * 数据授权
 */

var selectedUserIds = "";

function grant() {
    var selectNode = getSelectedNode();
    if (!selectNode) {
        showMessageBox("danger", "请选择位置，再进行数据授权!");
        return;
    }
    var locationId = selectNode["id"];
    //弹出选择用户框
    var url = "role/popUsers/" + locationId;
    $("#mBody").load(url,
        function (data) {
            $("#userListModal").modal("show");
        });
    //回调数据授权功能
}


/**
 * 数据授权
 */
function grantDataAuth() {
    var locationId = getSelectedNode().id;
    var userIds = selectedUsersId.join(",");
    if (!userIds) {
        showMessageBox("danger", "请选择要数据授权的用户！");
        return;
    }
    $("#userListModal").modal("hide");
    // ajax将选中的用户进行与角色关联
    var url = "authorityData/grantDataAuth";
    var data = {
        locationId: locationId,
        userIds: userIds
    };
    $.post(url, data, function (data) {
        if (data.result) {
            showMessageBox("info", data["resultDesc"]);
        }
    });
}


/**
 * 设置显示位置信息
 * @param data
 */
function setLocForm(data) {
    $("#description").val(data.description);
    $("#location").val(data.location);
    setFormReadStatus("#detailForm", true);
    loadUserList(data.id);
}


/**
 *
 * @param locationId
 */
function loadUserList(locationId) {
    //载入页面并刷新
    var url = "authorityData/loadUsers/" + locationId;
    $("#tab_1_1").load(url, function (data) {

    });
}


/**
 *
 * @param locationId 位置id
 */
function removeLocUser(userId) {
    var url = "user/removeLoc";
    var params = {userId: userId};
    $.post(url, params, function (data) {
        if (data) {
            showMessageBox("info", data["resultDesc"]);
            $("#usersInLocation").bootgrid("reload");
        } else {
            showMessageBox("danger", data["resultDesc"]);
        }

    });

}