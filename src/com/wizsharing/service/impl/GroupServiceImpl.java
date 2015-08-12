package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.Group;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired 
	private IBaseService<Group> baseService;
	
	@Override
	public List<Group> getGroupListPage(Page<Group> page) throws Exception{
		List<Group> list = this.baseService.findByPage("Group", null, null, null, null, page);
		return list;
	}

	@Override
	public Serializable doAdd(Group group) throws Exception {
		return this.baseService.add(group);
	}

	@Override
	public void doUpdate(Group group) throws Exception {
		this.baseService.update(group);
	}

	@Override
	public void doDelete(Group group) throws Exception {
		this.baseService.delete(group);
	}

	@Override
	public List<Group> getGroupList() throws Exception {
		List<Group> list = this.baseService.getAllList("Group", null, null);
		return list;
	}

	@Override
	public Group getGroupById(String id) throws Exception {
		return this.baseService.getUnique("Group", new String[]{"id"}, new String[]{id});
	}

}
