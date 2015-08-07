package com.wizsharing.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupAndResourceService;

@Service
public class GroupAndResourceServiceImpl implements IGroupAndResourceService {

	@Autowired 
	private IBaseService<GroupAndResource> baseService;
	 
	@Override
	public List<GroupAndResource> getResource(Integer groupId) throws Exception {
		List<GroupAndResource> list = this.baseService.findByWhere("GroupAndResource", new String[]{"groupId"}, new String[]{groupId.toString()},null, null);
		if (list == null) {
			return Collections.emptyList();
		}else{
			return list;
		}
	}

}
