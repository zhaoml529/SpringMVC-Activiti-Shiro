package com.wizsharing.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.entity.Resource;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IResourceService;
import com.wizsharing.util.BeanUtils;

@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired 
	private IBaseService<Resource> baseService;
	
	@Override
	public Resource getPermissions(Integer id) throws Exception {
		Resource res = this.baseService.getUnique("Resource", new String[]{"id", "available"}, new String[]{id.toString(), "1"});
		return res;
	}

	@Override
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception {
		List<Resource> menus = new ArrayList<Resource>();
		for(GroupAndResource gar : gr){
			Resource resource= getPermissions(gar.getResourceId());
			if(!BeanUtils.isBlank(resource)){
				if(resource.isRootNode()) {
	                continue;
	            }
	            if(!"menu".equals(resource.getType())) {
	                continue;
	            }
	            menus.add(resource);
			}
		}
		return menus;
	}

}
