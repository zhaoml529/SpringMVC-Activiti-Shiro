<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
</head>
<body>
	<hgroup>
		<h2><b>您的帐号在另一个地点登录，您已被踢出！</b></h2>
		<h2>点击登录按钮，重新登录。<a href="${ctx }/login?kickoutMsg=1" style="color: #08c;">登录</a>.</h2>
	</hgroup>
</body>
</html>
