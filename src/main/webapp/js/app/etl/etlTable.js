var listTab = $('#myTab li:eq(0) a');
var formTab = $('#myTab li:eq(1) a');
var object = null;
formName = "#detailForm";
$.ajaxSettings.async = false;
var validationConfig = {
    message: '该值无效 ',
    fields: {
        userName: {
            message: '用户名号无效',
            validators: {
                notEmpty: {
                    message: '用户名!'
                },
                stringLength: {
                    min: 3,
                    max: 20,
                    message: '用户名长度为3到20个字符'
                }
            }
        }
    }
};

$(function () {
    dataTableName = '#etlTableDataTable';
    docName = "元数据信息";
    mainObject = "etlTable";
    //初始化从数据库获取列表数据
    searchModel = [{"param": "tableDesc", "paramDesc": "表描述"}];
    initBootGridMenu(dataTableName, null);
    //初始化查询所有的
    ids = findAllRecordId();
    selectedIds = ids;
    validateForm.call(validationConfig);
    vdm = new Vue({
        el: formName,
        data: {
            etlTable: findById(selectedIds[pointer])
        }
    });
});






