<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="etlTableDataTable" class="table table-striped table-bordered table-hover" data-toggle="bootgrid" data-ajax="true"
       data-selection="true" data-multi-select="true" data-row-select="true" data-keep-selection="true"
       data-url="/etlTable/data">
    <thead>
    <tr>
        <th data-column-id="id" data-identifier="true" data-type="numeric" data-align="center" data-width="5%">序号</th>
        <th data-column-id="baseTableName" data-sortable="true" data-width="10%">基础表名称</th>
        <th data-column-id="serviceTableName" data-sortable="true" data-width="10%">业务表名称</th>
        <th data-column-id="tableDesc" data-sortable="true" data-width="10%">基础表描述</th>
        <th data-column-id="domainName" data-sortable="true" data-width="20%">类名</th>
        <th data-column-id="dropStatus" data-sortable="true" data-width="10%">是否可删除</th>
    </tr>
    </thead>
</table>