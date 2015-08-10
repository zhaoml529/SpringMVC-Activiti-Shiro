package com.wizsharing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.entity.Resource;
import com.wizsharing.entity.User;
import com.wizsharing.service.IGroupAndResourceService;
import com.wizsharing.service.IResourceService;
import com.wizsharing.service.IUserService;


/**
 * 首页控制器
 *
 * @author zml
 */
@Controller
public class MainController {

	@Autowired
	private IUserService userService;
	
    @Autowired
    private IGroupAndResourceService grService;
    
    @Autowired
    private IResourceService resourceService;
	
    @RequestMapping(value = "/north")
    public String index() {
        return "layout/north";
    }

    @RequestMapping(value = "/main")
    public String main() {
        return "layout/main";
    }
    
    @RequestMapping(value = "/center")
    public String center() {
    	return "layout/center";
    }
    
    @RequestMapping(value = "/south")
    public String nav(HttpSession session, Model model) throws Exception {
    	return "layout/south";
    }
    
    @RequestMapping("/menu")
    @ResponseBody
    public List<Resource> getMenu() throws Exception{
    	String username = (String) SecurityUtils.getSubject().getPrincipal();
    	User user = this.userService.getUserByName(username);
    	List<GroupAndResource> grList = this.grService.getResource(user.getGroup().getId());
    	List<Resource> menus = this.resourceService.getMenus(grList);
    	return menus;
    }
    
    @RequestMapping("/")
    public String index(Model model) throws Exception {
        return "index";
    }
    
}
