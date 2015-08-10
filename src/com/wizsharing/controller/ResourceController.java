package com.wizsharing.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wizsharing.entity.Message;
import com.wizsharing.entity.Resource;
import com.wizsharing.service.IResourceService;

/**
 * 资源控制器
 * @author ZML
 *
 */
@Controller
@RequiresPermissions("admin:*")
@RequestMapping(value = "/resource")
public class ResourceController {
	@Autowired
    private IResourceService resourceService;
	
	
	@RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(Resource resource) throws Exception{
		Message message = new Message();
		try {
			this.resourceService.doUpdate(resource);
			message.setStatus(Boolean.TRUE);
			message.setMessage("保存成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping(value = "/doUpdateName", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@RequestParam("id") String id, @RequestParam("name") String name) throws Exception {
		Message message = new Message();
		try {
			this.resourceService.doUpdateName(id, name);
			message.setStatus(Boolean.TRUE);
			message.setMessage("保存成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping(value = "/doDelete")
	@ResponseBody
	public Message doDelete(@RequestParam("id") String id) throws Exception {
		Message message = new Message();
		try {
			this.resourceService.doDelete(id);
			message.setStatus(Boolean.TRUE);
			message.setMessage("保存成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
}
