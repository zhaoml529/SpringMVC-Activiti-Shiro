package com.wizsharing.util;


public class Constants {
	
	/***************** system *****************/
	public static String DB_NAME = "mysql";
	public static String MESSAGE = "message";
	
	/***************** session key *****************/
    public static final String CURRENT_USER = "user";
    public static final String GROUP_ID = "groupId";
    public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    
	/***************** default password (123456) *****************/
	public static final String DEFAULT_PASSWORD = "14e1b600b1fd579f47433b88e8d85291";
	
	/***************** activiti *****************/
	public static final String ASSIGNEE = "assignee";
	public static final String CANDIDATE_USER = "candidateUser";
	public static final String CANDIDATE_GROUP = "candidateGroup";
	
	/***************** shiro *****************/
	public static final Integer PASSWORD_RETRY_COUNT = 5;	//登录次数锁定
	public static final Integer PASSWORD_SHOW_JCAPTCHA = 1;	//登录次数显示验证码
}
