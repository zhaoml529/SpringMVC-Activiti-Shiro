package com.wizsharing.controller;

import java.io.Serializable;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.entity.Message;
import com.wizsharing.entity.Resource;
import com.wizsharing.entity.User;
import com.wizsharing.service.IGroupAndResourceService;
import com.wizsharing.service.IResourceService;
import com.wizsharing.util.UserUtil;

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
	
	@Autowired
	private IGroupAndResourceService irService;
	
	/**
	 * 添加资源
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd")
	@ResponseBody
	public Message doAdd(Resource resource) throws Exception{
		resource.setAvailable(1);
		Message message = new Message();
		try {
			this.resourceService.doAdd(resource);
			message.setStatus(Boolean.TRUE);
			message.setMessage("添加成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("添加失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 在zTree上添加节点
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTree")
	@ResponseBody
	public Message addTree(Resource resource) throws Exception{
		resource.setType("menu");
		resource.setPermission("admin:tree:*");
		resource.setAvailable(1);
		Message message = new Message();
		User user = UserUtil.getUserFromSession();
		Serializable resourceId = this.resourceService.doAdd(resource);
		Integer groupId = user.getGroup().getId();
		this.irService.doAdd(new GroupAndResource(groupId, (Integer) resourceId));
		try {
			message.setStatus(Boolean.TRUE);
			message.setMessage("添加成功！");
			message.setData(resourceId);
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("添加失败！");
			throw e;
		}
		return message;
		
	}

	/**
	 * 更新
	 * @param resource
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 在zTree上更新节点
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateTreeName", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@RequestParam(value = "id") String id, @RequestParam("name") String name) throws Exception {
		Message message = new Message();
		try {
			Integer result = this.resourceService.doUpdateName(id, name);
			if(result > 0){
				message.setStatus(Boolean.TRUE);
				message.setMessage("保存成功！");
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("您所修改的内容不存在！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	@RequestMapping(value = "/updateTreeSort", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdateSort(@RequestParam(value = "sortArr", required = false) String sortArr, 
							@RequestParam(value = "nodeArr", required = false) String nodeArr) throws Exception {
		Message message = new Message();
		try {
			String[] nodes = nodeArr.split(",");
			String[] sorts = sortArr.split(",");
			for(int i=0;i<nodes.length;i++){
				String id = nodes[i];
				String sortNum = sorts[i];
				this.resourceService.doUpdateSort(id, sortNum);
			}
			message.setStatus(Boolean.TRUE);
			message.setMessage("顺序已调整！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doDelete")
	@ResponseBody
	public Message doDelete(@RequestParam("id") String id) throws Exception {
		Message message = new Message();
		try {
			Integer result = this.resourceService.doUpdateAvailable(id);
			if(result > 0){
				message.setStatus(Boolean.TRUE);
				message.setMessage("成功删除 "+result+" 条数据！");
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("您所删除的内容不存在！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败！");
			throw e;
		}
		return message;
	}
}
