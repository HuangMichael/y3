<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form class="form-horizontal"  id="locReportForm">
    <div class="row">
        <div class="col-md-12">
            <div class="form-group">
                <label for="applicant" class="col-md-2 control-label ">设备型号</label>
                <div class="col-md-10 ">
                    <input class="form-control " id="model" type="text " name="model" required/>
                </div>
            </div>
            <div class="form-group">
                <label for="applicant" class="col-md-2 control-label ">申请人</label>
                <div class="col-md-10 ">
                    <input class="form-control " id="applicant" type="text " name="applicant" required/>
                </div>
            </div>
            <div class="form-group">
                <label for="applyDep" class="col-md-2 control-label ">申请部门</label>
                <div class="col-md-10 ">
                    <input class="form-control " id="applyDep" type="text " name="applyDep" required/>
                </div>
            </div>
            <div class="form-group">
                <label for="applyDate" class="col-md-2 control-label ">申请日期</label>
                <div class="col-md-10 ">
                    <input class="Wdate form-control" id="applyDate" onLoad="WdatePicker({maxDate:'%y-%M-%d'})"
                           onClick="WdatePicker({maxDate:'%y-%M-%d'})" name="applyDate"
                           style="height:34px;border:1px solid #cccccc"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label" for="purpose">申请用途</label>
                <div class="col-md-10">
                    <input class="form-control" id="purpose" name="purpose"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label" for="approver">批准人</label>
                <div class="col-md-10">
                    <input class="form-control" id="approver" name="approver"/>
                </div>
            </div>
            <div class="form-group">
                <label for="handler" class="col-md-2 control-label ">经办人</label>
                <div class="col-md-10 ">
                    <input class="form-control" id="handler" name="handler"/>
                </div>
            </div>
            <div class="form-group">
                <label for="receiver" class="col-md-2 control-label ">接收人</label>
                <div class="col-md-10 ">
                    <input class="form-control " id="receiver" name="receiver" required/>
                    <input class="form-control " type="hidden" id="eqIds" name="eqIds"/>
                    <input class="form-control " type="hidden" id="locId" name="locations.id"/>
                    <input class="form-control " type="hidden" id="eqClassId" name="eqClass.id"/>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消
            </button>
            <button type="button" id="reportBtn" name="reportBtn" class="btn btn-primary btn-danger"
                    onclick="fillReport()">提交申请
            </button>
        </div>
</form>

