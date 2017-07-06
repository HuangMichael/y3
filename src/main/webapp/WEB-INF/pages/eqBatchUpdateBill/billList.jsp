<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="batchListTable" class=" table table-striped table-bordered table-hover" data-toggle="bootgrid"
       data-ajax="true" data-url="/eqBatchUpdateBill/data"
       data-selection="true" data-multi-select="true" data-row-select="true" data-keep-selection="true">
    <thead>
    <tr>
        <th data-column-id="id" data-width="5%">序号</th>
        <th data-column-id="id" data-type="numeric" data-identifier="true" data-visible="false" data-width="5%">ID</th>
        <th data-column-id="applyDate" data-width="10%">申请日期</th>
        <th data-column-id="applicant" data-width="5%">申请人</th>
        <th data-column-id="applyDep" data-width="10%">申请部门</th>
        <th data-column-id="purpose" data-width="10%">申请用途</th>
        <th data-column-id="billContent" data-width="20%">申请内容</th>
        <th data-column-id="approver" data-width="5%">申请人</th>
        <th data-column-id="handler" data-width="5%">经办人</th>
        <th data-column-id="receiver" data-width="5%">接收人</th>
        <th data-column-id="dataType" data-width="5%">更新状态</th>
    </tr>
    </thead>
</table>