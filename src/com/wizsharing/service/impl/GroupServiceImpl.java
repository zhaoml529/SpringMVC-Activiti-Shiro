package com.wizsharing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.Group;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired 
	private IBaseService<Group> baseService;
	
	@Override
	public List<Group> getGroupList() throws Exception{
		List<Group> list = this.baseService.getAllList("Group", null, null);
		return list;
	}

	@Override
	public Group getGroupById(String groupId) throws Exception {
		return this.baseService.getUnique("Group", new String[]{"id"}, new String[]{groupId});
	}

}
