package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.Group;
import com.wizsharing.entity.Parameter;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired 
	private IBaseService<Group> baseService;
	
	@Override
	public List<Group> getGroupListPage(Parameter param, Page<Group> page) throws Exception{
		List<Group> list = this.baseService.findListPage("Group", param, null, page);
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
		return this.baseService.findByWhere("Group", null);
	}

	@Override
	public Group getGroupById(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.baseService.findUnique("Group", map);
	}

}
