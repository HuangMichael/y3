<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<form class="form-horizontal myform" id="batchUpdateForm">
    <div class="form-group">
        <label for="applicant" class="col-md-2 control-label ">申请人</label>
        <div class="col-md-10 ">
            <input class="form-control " id="applicant" type="text " name="eqBatchUpdateBill.applicant" required
                   v-model="eqUpdateBill.applicant"/>
        </div>
    </div>
    <div class="form-group">
        <label for="applyDep" class="col-md-2 control-label ">申请部门</label>
        <div class="col-md-10 ">
            <input class="form-control " id="applyDep" type="text " name="eqBatchUpdateBill.applyDep" required
                   v-model="eqUpdateBill.applyDep"/>
        </div>
    </div>
    <div class="form-group">
        <label for="applyDate" class="col-md-2 control-label ">申请日期</label>
        <div class="col-md-10 ">
            <input class="Wdate form-control" id="applyDate" onLoad="WdatePicker({maxDate:'%y-%M-%d'})"
                   onClick="WdatePicker({maxDate:'%y-%M-%d'})" name="eqBatchUpdateBill.applyDate" v-model="eqUpdateBill.applyDate"
                   style="height:34px;border:1px solid #cccccc"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label" for="purpose">申请用途</label>
        <div class="col-md-10">
            <input class="form-control" id="purpose" name="eqBatchUpdateBill.purpose" v-model="eqUpdateBill.purpose"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label" for="approver">批准人</label>
        <div class="col-md-10">
            <input class="form-control" id="approver" name="eqBatchUpdateBill.approver" v-model="eqUpdateBill.approver"/>
        </div>
    </div>
    <div class="form-group">
        <label for="handler" class="col-md-2 control-label ">经办人</label>
        <div class="col-md-10 ">
            <input class="form-control" id="handler" name="eqBatchUpdateBill.handler" v-model="eqUpdateBill.handler"/>
        </div>
    </div>
    <div class="form-group">
        <label for="receiver" class="col-md-2 control-label ">接收人</label>
        <div class="col-md-10 ">
            <input class="form-control " id="receiver" name="eqBatchUpdateBill.receiver" required
                   v-model="eqUpdateBill.receiver "/>
        </div>
    </div>
    <div class="modal-footer ">
        <button  id="applyBtn" name="applyBtn" class="btn btn-primary btn-danger " onclick="commitApply()">保存记录
        </button>
    </div>
</form>