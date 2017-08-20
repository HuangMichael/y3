<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="box border blue">
    <div class="box-body">
        <%@include file="form.jsp" %>
    </div>
</div>
<div class="box border blue">
    <div class="box-body">
        <div class="box-body">
            <ul class="nav nav-tabs" id="myTab">
                <li class="active"><a data-toggle="tab" href="#tab_1_1">用户信息
                    <span class="badge badge-green" title="所有用户数量" id="userNum">${userList.size()}</span>
                </a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active " id="tab_1_1">

                </div>
            </div>
        </div>
    </div>
</div>
</div>
