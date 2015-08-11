<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<hgroup>
		<h2><b>404.</b> 抱歉! 您访问的资源不存在!</h2>
		<h2>给您带来的不便我们深表歉意! <a href="${ctx }/" target="_parent" style="color: #08c;">返回网站首页</a>.</h2>
	</hgroup>
</body>
</html>
