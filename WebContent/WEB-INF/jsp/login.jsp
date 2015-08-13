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
  <div id="main-wrapper">
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="navbar-header">
        <div class="logo"><h1>XXX系统</h1></div>
      </div>   
    </div>
    <div class="template-page-wrapper">
      <form class="form-horizontal templatemo-signin-form" role="form" name="formLogin" action="" id="formLogin" method="post">
      	<div id="errorMsg" class="form-group">
      		<div class="row">
		        <div id="msg" class="alert alert-danger sr-only" role="alert">
				  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				  <span class="sr-only">Error:</span>
				  ${msg }
 				   <button type="button" class="close" data-dismiss="alert" 
				      aria-hidden="true">
				      &times;
				   </button>
			    </div>
		    </div>
	    </div>
        <div id="username" class="form-group">
          <div class="col-sm-12">
            <label for="username" class="col-sm-2 control-label">用户名</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="name" value="${username }" placeholder="Username">
              <span id="username_sr" class="glyphicon glyphicon-remove form-control-feedback sr-only"></span>
            </div>
          </div>              
        </div>
        <div id="password" class="form-group">
          <div class="col-sm-12">
            <label for="password" class="col-sm-2 control-label">密&nbsp;&nbsp;码</label>
            <div class="col-sm-10">
              <input type="password" class="form-control" name="passwd" placeholder="Password">
              <span id="password_sr" class="glyphicon glyphicon-remove form-control-feedback sr-only"></span>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-12">
            <div class="col-sm-offset-2 col-sm-10">
                <div class="form-inline">
	              	<div class="checkbox">
		                <label>
		                    <input type="checkbox" name="rememberMe" value="0"> Remember me
		                </label>
					</div>
	                <c:if test="${jcaptcha}">
						<input class="form-control" style="width: 20%; margin-left: 35%;" name="jcaptchaCode" id="jcaptchaCode" type="text" placeholder="验证码" nullmsg="请输入验证码!" />
						<img style="padding: 2px 2px;width: 20%;" align="absmiddle" id="jcaptcha" src="${ctx }/jcaptcha.jpg"/>
					</c:if>
              </div>
            </div>
<!--             <div class="col-sm-2"></div>
            <div class="col-sm-5 form-inline input-group">
            	
            </div> -->
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-12">
            <div class="col-sm-offset-2 col-sm-10">
              <input type="submit" value="登录" class="btn btn-default">
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
<script>
	$(function() {
	    $("#jcaptcha").click(function() {
	        $("#jcaptcha").attr("src", '${ctx}/jcaptcha.jpg?'+new Date().getTime());
	    });
	    var msg = '${msg}';
	  	if(msg != ''){
	  		$("#msg").removeClass("sr-only");
	  	}
	});
</script>
</body>
</html>