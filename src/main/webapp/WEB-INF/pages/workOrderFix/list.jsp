<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container">
    <div class="row">
        <div id="content" class="col-lg-12">
            <!-- PAGE HEADER-->
            <%@include file="../common/common-breadcrumb.jsp" %>
            <div class="row">
                <div class="col-md-12">
                    <!-- BOX -->
                    <div class="box border blue">
                        <div class="box-title">
                            <h4><i class="fa fa-sitemap"></i>维修单查询</h4>
                        </div>
                        <%@include file="../common/common-menubar.jsp" %>
                        <div class="box-body">
                            <div id="contentDiv">
                                <div class="box-body">
                                    <div class="tabbable">
                                        <ul class="nav nav-tabs" id="myTab">
                                            <li class="active">
                                                <a href="#tab_1_0" data-toggle="tab">
                                                    <i class="fa fa-user" id="eq"></i>维修单查询</a>
                                            </li>
                                            <li>
                                                <a href="#tab_1_1" data-toggle="tab">
                                                    <i class="fa fa-clock-o" id="expiredTab"></i>超期维修单
                                                    <div id="expiredTip" class="badge badge-red"></div>
                                                </a>
                                            </li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane fade in active" id="tab_1_0">
                                                <div class="form-group" style="margin-bottom:10px;position:inherit"
                                                     id="searchBox">

                                                    <div class="col-md-1">
                                                        <select class="form-control" id="nodeState" name="nodeState"
                                                                onchange="searchMore()"
                                                                required>
                                                            <option selected>已派工</option>
                                                            <option>已完工</option>
                                                            <option>已暂停</option>
                                                            <option>已取消</option>
                                                        </select>
                                                    </div>

                                                    <div class="col-md-1">
                                                        <input class="form-control" id="eqCode" type="text"
                                                               name="orderLineNo"
                                                               placeholder="跟踪号"/>
                                                    </div>
                                                    <div class="col-md-1">
                                                        <input class="form-control" id="orderDesc" type="text"
                                                               name="orderDesc"
                                                               placeholder="故障描述"/>
                                                    </div>

                                                    <div class="col-md-1">
                                                        <input class="form-control" id="locName" type="text"
                                                               name="orderDesc"
                                                               placeholder="位置"/>
                                                    </div>

                                                    <div class="col-md-2">
                                                        <select class="form-control" id="eqClass" name="eqClass"
                                                                style="width:100%"
                                                                required>
                                                            <option></option>
                                                            <template v-for="option in eqClasses">
                                                                <option :value="option.cname">
                                                                    {{option.cname}}
                                                                </option>
                                                            </template>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-1">
                                                        <select class="form-control" id="expired" name="expired"
                                                                onchange="searchMore()"
                                                                required>
                                                            <option selected>未超期</option>
                                                            <option>已超期</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-1">
                                                        <button id="searchBtn" class="btn btn-default"
                                                                onclick="searchMore()">查询
                                                        </button>
                                                    </div>
                                                </div>
                                                <table id="fixListTable0"
                                                       class="table table-striped table-bordered table-hover  table-responsive"
                                                       data-toggle="bootgrid" data-ajax="true" data-rowSelect="true"
                                                       data-selection="true"
                                                       data-url="/workOrderFix/data">
                                                    <thead>
                                                    <tr>
                                                        <th data-column-id="orderLineNo" data-width="5%">跟踪号</th>
                                                        <th data-column-id="id" data-type="numeric"
                                                            data-identifier="true" data-visible="false">ID
                                                        </th>
                                                        <th data-column-id="eqName" data-width="6%">设备名称</th>
                                                        <th data-column-id="locName" data-width="12%">设备位置</th>
                                                        <th data-column-id="orderDesc" data-width="15%">故障描述</th>
                                                        <th data-column-id="eqClass" data-width="8%">设备分类</th>
                                                        <th data-column-id="unitName" data-width="10%"
                                                            data-visible="false">维修单位
                                                        </th>
                                                        <th data-column-id="nodeState" data-width="5%">维修状态</th>
                                                        <th data-column-id="nodeTime" data-width="10%" data-order="desc">
                                                            处理时间
                                                        </th>
                                                        <th data-column-id="deadLine" data-width="10%">截止日期</th>
                                                        <th data-column-id="expired" data-width="5%"
                                                            data-sortable="false" align="center">是否超期
                                                        </th>
                                                    </tr>
                                                    </thead>
                                                </table>
                                            </div>
                                            <div class="tab-pane fade in " id="tab_1_1">
                                                <table id="expiredOrdersList"
                                                       class="table table-striped table-bordered table-hover  table-responsive"
                                                       data-toggle="bootgrid" data-ajax="true"
                                                       data-url="/workOrderFix/expiredCount">
                                                    <thead>
                                                    <tr>
                                                        <th data-column-id="orderLineNo" data-width="8%">跟踪号</th>
                                                        <th data-column-id="eqName" data-width="10%">设备名称</th>
                                                        <th data-column-id="locName" data-width="8%">设备位置</th>
                                                        <th data-column-id="orderDesc" data-width="15%">故障描述</th>
                                                        <th data-column-id="eqClass" data-width="5%">设备分类</th>
                                                        <th data-column-id="unitName" data-width="10%"
                                                            data-visible="false">维修单位
                                                        </th>
                                                        <th data-column-id="nodeState" data-width="5%">维修状态</th>
                                                        <th data-column-id="nodeTime" data-width="8%" data-order="desc">
                                                            处理时间
                                                        </th>
                                                        <th data-column-id="deadLine" data-width="8%">截止日期</th>
                                                    </tr>
                                                    </thead>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../common/common-back2top.jsp" %>
</div>

<div class="modal fade " id="fix_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel2">维修单明细信息</h4>
            </div>
            <div class="modal-body" id="modal_div">
                <%@include file="form.jsp" %>
            </div>
        </div>
    </div>
</div>

<div class="modal fade " id="fix_desc_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="fix_desc_modal_label">请输入维修描述</h4>
            </div>
            <div class="modal-body" id="fix_desc_modal_div">
                <%@include file="fixDescForm.jsp" %>
            </div>
        </div>
    </div>
</div>

<div class="modal fade " id="fix_adjust_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="fix_adjust_modal_desc">请选择调整后的时间</h4>
            </div>
            <div class="modal-body" id="fix_adjust_modal_div">
                <%@include file="fixAdujstForm.jsp" %>
            </div>
        </div>
    </div>
</div>


<!-- SAMPLE BOX CONFIGURATION MODAL FORM-->
<div class="modal fade" id="eqInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">设备详细信息</h4>
            </div>
            <div class="modal-body" id="mBody1">
                <form class="form-horizontal" role="form" id="formEq">
                    <div class="form-group">
                        <label for="eqNo" class="col-md-2 control-label">设备编号</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="eqNo" type="text" name="eqNo" READONLY/>
                        </div>
                        <label for="eqName" class="col-md-2 control-label">设备名称</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="eqName" type="text" name="eqName" READONLY/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="eqNo" class="col-md-2 control-label">设备位置</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="location" type="text" name="eqNo" READONLY/>
                        </div>
                        <label for="eqName" class="col-md-2 control-label">设备分类</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="eqClass" type="text" name="eqClass" READONLY/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="eqNo" class="col-md-2 control-label">设备型号</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="eqModel" type="text" name="eqModel" READONLY/>
                        </div>
                        <label for="eqName" class="col-md-2 control-label">生产厂家</label>
                        <div class="col-md-4 ">
                            <input class="form-control" id="productFactory" type="text" name="productFactory" READONLY/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<div class="modal fade " id="loc_modal" tabindex="-1"
     role="dialog" aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel3">请输入设备更新申请信息</h4>
            </div>
            <div class="modal-body">
                <%@include file="applyReport.jsp" %>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/js/app/fix/fix.js"></script>