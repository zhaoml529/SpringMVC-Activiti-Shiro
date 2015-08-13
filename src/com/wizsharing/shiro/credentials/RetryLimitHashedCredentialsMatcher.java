package com.wizsharing.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import com.wizsharing.util.Constants;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 输错5次密码锁定2分钟，ehcache.xml配置
 * @author ZML
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;
    
    private Cache<String, AtomicBoolean> jcaptchaCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        jcaptchaCache = cacheManager.getCache("jcaptchaCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        //密码输错3次，显示验证码
        if(retryCount.incrementAndGet() > Constants.PASSWORD_SHOW_JCAPTCHA ) {
        	AtomicBoolean enabled = jcaptchaCache.get("jcaptchaEnabled");
        	if(enabled == null) {
        		enabled = new AtomicBoolean(true);
        		jcaptchaCache.put("jcaptchaEnabled", enabled);
        	}
        }
        //多于5次锁定2分钟
        if(retryCount.get() > Constants.PASSWORD_RETRY_COUNT) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        //匹配用户输入的token的凭证（未加密）与系统提供的凭证（已加密）  
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
    
}
