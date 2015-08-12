<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String easyuiThemeName="metro";
	Cookie cookies[] =request.getCookies();
	if(cookies!=null&&cookies.length>0){
		for(Cookie cookie : cookies){
			if (cookie.getName().equals("cookiesColor")) {
				easyuiThemeName = cookie.getValue();
				break;
			}
		}
	}
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.2.1.min.js"></script>

<link rel="stylesheet" href="${ctx}/css/login/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/login/login_main.css" type="text/css" />

<link rel="stylesheet" href="${ctx}/css/zTreeStyle/zTreeStyle.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/themes/<%=easyuiThemeName %>/easyui.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/common.css">

<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/js/moment.min.js"></script>

<script type="text/javascript" src="${ctx}/js/login/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/login/templatemo_script.js"></script>

<script type="text/javascript" src="${ctx}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jqueryUtil.js"></script>

<script type="text/javascript" charset="utf-8">
	var ctx = "${ctx}";
</script>

