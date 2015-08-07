package com.wizsharing.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.service.IActivitiIdentityService;

/**
 * 与 Activiti 的 Identity相关表做关联
 * @author ZML
 *
 */
@Service
public class ActivitiIdentityServiceImpl implements IActivitiIdentityService {

	@Autowired
	protected IJdbcDao jdbcDao;
	
	@Override
	public void deleteAllUser() {
		String sql = "delete from ACT_ID_USER";
		this.jdbcDao.delete(sql, null);
	}

	@Override
	public void deleteAllRole() {
		String sql = "delete from ACT_ID_GROUP";
		this.jdbcDao.delete(sql, null);
	}

	@Override
	public void deleteAllMemerShip() {
		String sql = "delete from ACT_ID_MEMBERSHIP";
		this.jdbcDao.delete(sql, null);
	}

	@Override
	public void updateMembership(String userId, String groupId) throws Exception {
		String sql = "update ACT_ID_MEMBERSHIP set GROUP_ID_=:groupId where USER_ID_=:userId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("groupId", groupId);  
	    paramMap.put("userId", userId);  
		this.jdbcDao.saveOrUpdate(sql, paramMap);
		
	}

}
