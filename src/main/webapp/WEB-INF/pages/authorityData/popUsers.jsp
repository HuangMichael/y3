<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="usersNotInLocation" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th data-column-id="id">序号</th>
        <th data-column-id="id" data-type="numeric" data-identifier="true" data-visible="false" data-width="15%">ID</th>
        <th data-column-id="userName" data-width="75%">用户名</th>

        <th data-column-id="command" data-formatter-id="remove" data-width="10%">用户名</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${usersNotInLocation}" var="u" varStatus="s">
        <tr class="gradeX">
            <td>${s.index+1}</td>
            <td>
                    ${u[0]}
            </td>
            <td>
                    ${u[1]}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
    var selectedUsersId = [];
    $("#usersNotInLocation").bootgrid({
        selection: true,
        multiSelect: true,
        rowSelect: false,
        keepSelection: true
    }).on("selected.rs.jquery.bootgrid", function (e, rows) {
        for (var x in rows) {
            if (rows[x]["id"]) {
                selectedUsersId.push(rows[x]["id"]);
            }
        }
    }).on("deselected.rs.jquery.bootgrid", function (e, rows) {
        for (var x in rows) {
            selectedUsersId.remove(rows[x]["id"]);
        }
    });
</script>