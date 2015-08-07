package com.wizsharing.service;

import java.util.List;

import com.wizsharing.entity.GroupAndResource;

public interface IGroupAndResourceService {

	public List<GroupAndResource> getResource(Integer groupId) throws Exception;
}

