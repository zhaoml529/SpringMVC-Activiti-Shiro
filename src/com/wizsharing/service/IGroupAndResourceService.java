package com.wizsharing.service;

import java.util.List;

import com.wizsharing.entity.GroupAndResource;

public interface IGroupAndResourceService {

	public List<GroupAndResource> getResource(Integer groupId) throws Exception;
	
	public void doAdd(GroupAndResource gar) throws Exception;
	
	public void doDelete(GroupAndResource gar) throws Exception;
	
	public Integer doDelByGroup(Integer groupId) throws Exception;
	
	public void doDelByResource(String resourceId) throws Exception;
}


