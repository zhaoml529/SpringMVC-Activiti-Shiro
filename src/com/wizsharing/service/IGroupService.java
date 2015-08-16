package com.wizsharing.service;

import java.io.Serializable;
import java.util.List;

import com.wizsharing.entity.Group;
import com.wizsharing.entity.Parameter;
import com.wizsharing.pagination.Page;

public interface IGroupService {

	public List<Group> getGroupListPage(Parameter param, Page<Group> page) throws Exception;
	
	public List<Group> getGroupList() throws Exception;
	
	public Group getGroupById(String id) throws Exception;
	
	public Serializable doAdd(Group group) throws Exception;

	public void doUpdate(Group group) throws Exception;
	
	public void doDelete(Group group) throws Exception;
	
}
