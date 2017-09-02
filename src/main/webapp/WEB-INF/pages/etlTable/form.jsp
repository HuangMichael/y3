<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form class="form-horizontal myform" id="detailForm">
    <div class="form-group">
        <label class="col-md-2 col-sm-2 col-lg-2" for="baseTableName">基础表名</label>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <input type="hidden" class="form-control" id="id" name="id" v-model="etlTable.id" readonly>
            <input class="form-control" id="baseTableName" name="baseTableName" v-model="etlTable.baseTableName">
        </div>
        <div class="col-md-2 col-sm-2 col-lg-2">
            <label for="serviceTableName">业务表名</label>
        </div>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <input class="form-control" id="serviceTableName" name="serviceTableName" v-model="etlTable.serviceTableName">
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-2 col-sm-2 col-lg-2">
            <label for="serviceTableName">类名</label>
        </div>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <input class="form-control" id="domainName" name="domainName" v-model="etlTable.domainName">
        </div>
        <div class="col-md-2 col-sm-2 col-lg-2">
            <label for="status">删除状态</label>
        </div>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <select class="form-control" id="dropStatus" name="dropStatus" required v-model="etlTable.dropStatus"
                    style="width:100%"
                    required>
                <option value="1" selected>启用</option>
                <option value="0">禁用</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-2 col-sm-2 col-lg-2">
            <label for="serviceTableName">基础表描述</label>
        </div>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <input class="form-control" id="tableDesc" name="tableDesc" v-model="etlTable.tableDesc">
        </div>

        <div class="col-md-2 col-sm-2 col-lg-2">
            <label for="status">状态</label>
        </div>
        <div class="col-md-4 col-sm-4 col-lg-4">
            <select class="form-control" id="status" name="status" required v-model="etlTable.status" style="width:100%"
                    required>
                <option value="1" selected>启用</option>
                <option value="0">禁用</option>
            </select>
        </div>
    </div>
    <div class="modal-footer">
        <button id="saveBtn" name="saveBtn" class="btn btn-primary btn-danger">保存记录
        </button>
    </div>
</form>

