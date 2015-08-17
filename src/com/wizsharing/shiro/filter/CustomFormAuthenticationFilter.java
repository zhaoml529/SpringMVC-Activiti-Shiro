package com.wizsharing.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wizsharing.entity.User;
import com.wizsharing.service.IUserService;
import com.wizsharing.util.BeanUtils;
import com.wizsharing.util.Constants;
import com.wizsharing.util.UserUtil;

/**
 * 验证验证码的拦截器
 * @author zml
 *
 */

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	@Autowired
	private IUserService userService;
	
    //当验证码验证失败时不再走身份认证拦截器
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if(request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
        return super.onAccessDenied(request, response, mappedValue);
    }
    
    //可以根据不同角色设置跳转不同页面
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
            Subject subject,
            ServletRequest request,
            ServletResponse response)
                 throws Exception {
    	boolean contextRelative = true;
    	Session session = subject.getSession();
    	if(session != null){
    		//会话过期 attribute 为空，重新设置。
    		User user = (User) session.getAttribute(Constants.CURRENT_USER);
    		if(BeanUtils.isBlank(user)){
    			user = this.userService.getUserByName(subject.getPrincipal().toString());
    			UserUtil.saveUserToSession(session, user);
    		}
    	}
    	String successUrl = this.getSuccessUrl();
    	SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
    	if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
            successUrl = savedRequest.getRequestUrl();
            contextRelative = false;
        }
        WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);
		return false;
    	
    }
}
