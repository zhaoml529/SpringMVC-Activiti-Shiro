package com.wizsharing.service;

import java.util.List;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.entity.Resource;
import com.wizsharing.pagination.Page;

public interface IResourceService {

	public Resource getPermissions(Integer id) throws Exception;
	
	public Resource getResource(String id) throws Exception;
	
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception;
	
	public List<Resource> getAllResource() throws Exception;
	
	public List<Resource> getResourceList(Page<Resource> p) throws Exception;
	
	public void doAdd(Resource entity) throws Exception;
	
	public void doUpdate(Resource entity) throws Exception;
	
	public void doUpdateName(String id) throws Exception;
	
	public void doDelete(Resource entity) throws Exception;
	
	public void doDelete(String id) throws Exception;
}
