<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" charset="utf-8">
	function logout() {
		$.messager.confirm("提示", "确认退出吗?",function(r){
			if(r){
				window.location.href="${ctx}/logout";
			}
		});
		
	}

	var userInfoWindow;
	function showUserInfo() {
		userInfoWindow = $('<div/>').window({
			modal : true,
			title : '当前用户信息',
			width : 350,
			height : 300,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			//href : 'userAction!showUserInfo.action',
			onClose : function() {
				$(this).window('destroy');
			}
		});
	}
</script>
<div style="position: absolute; right: 10px; bottom: 5px; ">
	<shiro:principal/>&nbsp;&nbsp;你好，欢迎登录！&nbsp;&nbsp;
	<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">控制面板</a> 
</div>
<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	<div onclick="showUserInfo();">个人信息</div>
	<div class="menu-sep"></div>
	<div onclick="logout();">退出系统</div>
</div>
