<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form class="form-horizontal" id="detailForm">
    <div class="row">
        <div class="col-md-12 col-sm-12 col-lg-12">
            <div class="form-group">
                <label for="description" class="col-md-2 col-sm-2 col-lg-2 control-label">位置编号</label>
                <div class="col-md-3 col-sm-3 col-lg-3">
                    <input class="form-control" id="location" name="location"
                           value="${location.location}" v-model="location.location" required/>
                </div>
                <label for="description" class="col-md-2 col-sm-2 col-lg-2 control-label">位置名称</label>
                <div class="col-md-3 col-sm-3 col-lg-3">
                    <input class="form-control" id="description" name="description"
                           value="${location.description}" v-model="location.description" required/>
                </div>
            </div>
        </div>
    </div>
</form>