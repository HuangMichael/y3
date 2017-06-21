<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <h4><i class="fa fa-sitemap"></i>设备更新信息</h4>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <!-- BOX -->
                    <div class="box border blue">
                        <div class="box-body treeContainer" id="treeDiv">
                            <ul id="tree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="box border blue">
                        <%@include file='../common/common-menubar.jsp' %>
                    </div>
                    <div class="divide-2"></div>
                    <!-- BOX -->
                    <div id="contentDiv">
                        <%@include file="detail.jsp" %>
                    </div>
                </div>
            </div>
            <%@include file="../common/common-back2top.jsp" %>
        </div>
    </div>
    <!-- /CONTENT-->
</div>
<div class="modal fade " id="loc_modal" tabindex="-1"
     role="dialog" aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel2">请输入设备更新申请信息</h4>
            </div>
            <div class="modal-body">
                <%@include file="locationReport.jsp" %>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/app/eqBatchUpdate/eqBatchUpdate.js"></script>
