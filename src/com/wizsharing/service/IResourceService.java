package com.wizsharing.service;

import java.util.List;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.entity.Resource;

public interface IResourceService {

	public Resource getPermissions(Integer id) throws Exception;
	
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception;
}
