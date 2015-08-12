package com.wizsharing.service;

import java.io.Serializable;
import java.util.List;

import com.wizsharing.entity.Resource;
import com.wizsharing.pagination.Page;

public interface IResourceService {

	public Resource getPermissions(Integer id) throws Exception;
	
	public Resource getResource(String id) throws Exception;
	
	public List<Resource> getTree(Integer groupId) throws Exception;
	
	public List<Resource> getAllResource() throws Exception;
	
	public List<Resource> getResourceList(Page<Resource> p) throws Exception;
	
	public Serializable  doAdd(Resource entity) throws Exception;
	
	public void doUpdate(Resource entity) throws Exception;
	
	public Integer doUpdateAvailable(String id) throws Exception;
	
	public Integer doUpdateName(String id, String name) throws Exception;
	
	public Integer doUpdateSort(String id, String sort) throws Exception;
	
	public void doDelete(Resource entity) throws Exception;
	
	public Integer doDelete(String id) throws Exception;
	
}
