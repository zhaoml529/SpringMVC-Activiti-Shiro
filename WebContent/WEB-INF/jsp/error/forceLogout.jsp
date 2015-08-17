<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
</head>
<body>
	<hgroup>
		<h2><b>您已经被管理员强制退出!</b></h2>
		<h2>点击登录按钮，重新登录。<a href="${ctx }/login?forceLogoutMsg=1" style="color: #08c;">登录</a>.</h2>
	</hgroup>
</body>
</html>
