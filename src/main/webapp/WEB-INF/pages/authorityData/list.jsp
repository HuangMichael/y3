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
                            <h4><i class="fa fa-sitemap"></i>数据授权</h4>
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
<!-- SAMPLE BOX CONFIGURATION MODAL FORM-->
<div class="modal fade" id="userListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" >&times;</button>
                <h4 class="modal-title">请选择数据授权要添加的用户</h4>
            </div>
            <div class="modal-body" id="mBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">取消
                </button>
                <button type="button" id="confirmBtn" name="confirmBtn" onclick="grantDataAuth()"
                        class="btn btn-danger">确定
                </button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/js/zTree_v3-master/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/js/app/authorityData/authorityData.js"></script>
