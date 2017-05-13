<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="fixListTable" class="table table-bordered table-hover table-striped">
    <thead>
    <tr>
        <th data-column-id="orderLineNo" data-width="10%">设备位置</th>
        <th data-column-id="orderDesc" data-width="20%">设备分类</th>
        <th data-column-id="orderDesc" data-width="20%">设备名称</th>
        <th data-column-id="orderDesc" data-width="20%">故障描述</th>
        <th data-column-id="nodeTime" data-width="10%">报修时间</th>
    </tr>

    </thead>
    <tbody>
    <c:forEach items="${fixClassHistoryList}" var='r'>
        <tr>
            <td>${r.locations.description}</td>
            <td>${r.equipmentsClassification.description}</td>
            <td>${r.equipments.description}</td>
            <td>${r.orderDesc}</td>
            <td>${r.reportTime}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>