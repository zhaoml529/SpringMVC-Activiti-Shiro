<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>
<div style="margin-top: 90px; ">
	<div style="height: 40px;"></div>
	<div style="height: 40px; margin-top: 100px;">
		<h1 style="font-size:30px; margin-top: 100px;"><strong>欢迎使用OA系统</strong></h1>
    </div>
	
	<div class="formDiv">
		<span style="color:red; height: 30px;">${msg}</span>
		<form method="post" action="">
			<div class="textDiv">
				<span style="width: 300px; margin-right: 20px;">用户名: </span>
				<input type="text" name="name" value="<shiro:principal/>"/>
			
			</div>
			<div class="textDiv">
				<span style="width: 300px; margin-right: 40px;">密码:</span> 
				<input type="password" name="passwd"/>
			</div>
			<div class="textDiv">
			<c:if test="${jcaptchaEbabled}"> 
				<span style="width: 300px; margin-right: 40px;">验证码:</span> 
				<input type="text" name="jcaptchaCode"> 
				<img class="jcaptcha-btn jcaptcha-img" src="${ctx}/jcaptcha.jpg" title="点击更换验证码"> 
				<a class="jcaptcha-btn" href="javascript:;">换一张</a> 
				</c:if> 
			</div>
			<div class="textDiv">
				<span style="width: 300px; margin-right: 40px;">记住我</span> 
				<input type="checkbox" name="rememberMe" value="true">
			</div>
			<div class="buttonDiv">
				<input type="submit" value="确定"/>
				<input type="button" value="注册" onclick="window.location.href='${ctx }/userAction/toAdd'"/>
			</div>
		</form>
	</div>
</div>
<script>
    $(function() {
        $(".jcaptcha-btn").click(function() {
            $(".jcaptcha-img").attr("src", '${ctx}/jcaptcha.jpg?'+new Date().getTime());
        });
    });
</script>
</body>
</html>