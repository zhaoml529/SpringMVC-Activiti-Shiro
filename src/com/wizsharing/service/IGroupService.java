package com.wizsharing.service;

import java.util.List;

import com.wizsharing.entity.Group;

public interface IGroupService {

	public List<Group> getGroupList() throws Exception;
	
	public Group getGroupById(String groupId) throws Exception;
	
}
