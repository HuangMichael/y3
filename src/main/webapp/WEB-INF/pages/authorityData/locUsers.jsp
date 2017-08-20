<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="usersInLocation" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th data-column-id="id">序号</th>
        <th data-column-id="id" data-type="numeric" data-identifier="true" data-visible="false" data-width="5%">ID</th>
        <th data-column-id="userName" data-width="85%">用户名</th>
        <th data-column-id="command" data-formatter="command" data-width="10%">取消授权</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${usersInLocation}" var="u" varStatus="s">
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
    $("#usersInLocation").bootgrid({
        rowCount: 8,
        rowSelect: false,
        keepSelection: true,
        formatters: {
            "command": function (column, row) {
                return '<a class="btn btn-default btn-xs"  onclick="removeLocUser(' + row.id + ')" title="取消授权" ><i class="glyphicon glyphicon-remove"></i></a>'
            }
        }
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
    })


    ;
</script>