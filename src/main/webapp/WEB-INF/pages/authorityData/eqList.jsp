<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<table class="table table-striped table-bordered table-hover" style="max-height: 200px;overflow: scroll">
    <thead>
    <tr>
        <th>序号</th>
        <th>用户名</th>
        <th>人员名称</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reportedEqList}" var="c" varStatus="s">
        <tr class="gradeX">
            <td>${s.index+1}</td>
            <td>
                    ${c.vlocations.locName}
            </td>
            <td>
                    ${c.orderDesc}
            </td>
            <td>
                    ${c.reportTime}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

