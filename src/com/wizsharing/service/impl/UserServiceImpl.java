package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.entity.Group;
import com.wizsharing.entity.User;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IActivitiIdentityService;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupService;
import com.wizsharing.service.IUserService;
import com.wizsharing.util.BeanUtils;


/**
 * 同步或者重构Activiti Identify用户数据的多种方案比较
 * 参考：http://www.kafeitu.me/activiti/2012/04/23/synchronize-or-redesign-user-and-role-for-activiti.html
 * @ClassName: UserServiceImpl
 * @Description:user实现类，开启事务
 * @author: zml
 * @date: 2014-11-9 上午12:53:20
 *
 */

@Service
public class UserServiceImpl implements IUserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
    @Autowired
    protected PasswordHelper passwordHelper;
    
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected IActivitiIdentityService activitIdentityService;
    
    @Autowired
    protected IGroupService groupService;
    
    @Autowired
    private IBaseService<User> baseService;

	@Override
	public User getUserByName(String user_name) throws Exception{
		User user = this.baseService.getUnique("User", new String[]{"name"}, new String[] {user_name});
		if(BeanUtils.isBlank(user)){
			return null;
		}else{
			return user;
		}
	}

	@Override
	public List<User> getUserByGroupId(String groupId, Page<User> page) throws Exception {
		List<User> list = this.baseService.findByPage("User", new String[]{"group"}, new String[]{groupId}, new String[]{"id"}, new String[]{"DESC"}, page);
		return list;
	}
	
	@Override
	public List<User> getUserList(Page<User> page) throws Exception{
		List<User> list = this.baseService.findByPage("User", null, null, new String[]{"id"}, new String[]{"DESC"}, page);
		return list;
	}

	@Override
	public User getUserById(Integer id) throws Exception {
		return this.baseService.getUnique("User", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public void doUpdate(User user) throws Exception {
		//pwd 为修改后的
		this.passwordHelper.encryptPassword(user);
		this.baseService.update(user);
		updateMembership(user);
	}

	/**
	 * 保存用户信息，并且同步用户信息到activiti的identity.User和identify.Group
	 * @param synToActiviti     是否同步数据到Activiti
	 */
	
	@Override
	public Serializable doAdd(User user, boolean synToActiviti) throws Exception {
		//加密密码
        this.passwordHelper.encryptPassword(user);
        //添加用户
        Serializable userId = this.baseService.add(user);
        
//        if(userId != null){
//        	throw new RuntimeException("回滚");
//        }
        
        // 同步数据到Activiti Identify相关表
        if (synToActiviti) {
        	UserQuery userQuery = identityService.createUserQuery();
            List<org.activiti.engine.identity.User> activitiUsers = userQuery.userId(userId.toString()).list();
 
            if (activitiUsers.size() == 1) {
                updateActivitiData(user, activitiUsers.get(0));
            } else if (activitiUsers.size() > 1) {
                String errorMsg = "发现重复用户：id=" + userId;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            } else {
                newActivitiUser(user);
            }
        }
        
        return userId;
        
	}

	/**
     * 添加工作流用户以及角色
     * @param user      用户对象{@link User}
     * @param roleIds   用户拥有的角色ID集合
     */
    private void newActivitiUser(User user) {
        String userId = user.getId().toString();
        String groupId = user.getGroup().getId().toString();
 
        if(StringUtils.isBlank(groupId)){
        	logger.error("groupId 为空不能同步membership");
        	throw new RuntimeException("groupId 为空不能同步membership");
        }else{
        	// 添加用户
        	saveActivitiUser(user);
        	
        	// 添加membership
        	addMembershipToIdentify(userId, groupId);
        }
    }
	
    /**
     * 添加一个用户到Activiti {@link org.activiti.engine.identity.User}
     * @param user  用户对象, {@link User}
     */
    private void saveActivitiUser(User user) {
        String userId = user.getId().toString();
        org.activiti.engine.identity.User activitiUser = identityService.newUser(userId);
        cloneAndSaveActivitiUser(user, activitiUser);
        logger.info("add activiti user: {}"+ToStringBuilder.reflectionToString(activitiUser));
    }
    
    
	/**
     * 更新工作流用户以及角色
     * @param user          用户对象{@link User}
     * @param roleIds       用户拥有的角色ID集合
     * @param activitiUser  Activiti引擎的用户对象，{@link org.activiti.engine.identity.User}
     */
    private void updateActivitiData(User user, org.activiti.engine.identity.User activitiUser) {
 
        String userId = user.getId().toString();
        String groupId = user.getGroup().getId().toString();
 
        // 更新用户主体信息
        cloneAndSaveActivitiUser(user, activitiUser);
 
        // 删除用户的membership
        List<org.activiti.engine.identity.Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
        for (org.activiti.engine.identity.Group group : activitiGroups) {
        	//把类对应的基本属性和值输出来
            logger.info("delete group from activit: {}" + ToStringBuilder.reflectionToString(group));
            identityService.deleteMembership(userId, group.getId());
        }
 
        // 添加membership
        addMembershipToIdentify(userId, groupId);
    }
	
    /**
     * 使用系统用户对象属性设置到Activiti User对象中
     * @param user          系统用户对象
     * @param activitiUser  Activiti User
     */
    private void cloneAndSaveActivitiUser(User user, org.activiti.engine.identity.User activitiUser) {
        activitiUser.setFirstName(user.getName());
        activitiUser.setLastName(StringUtils.EMPTY);
        activitiUser.setPassword(StringUtils.EMPTY);
        activitiUser.setEmail(StringUtils.EMPTY);
        identityService.saveUser(activitiUser);
    }
    
    /**
     * 添加Activiti Identify的用户于组关系
     * @param groupId   角色ID集合
     * @param userId    用户ID
     */
    private void addMembershipToIdentify(String userId, String groupId) {
    	
        identityService.createMembership(userId, groupId);
    }
    
    /**
     * 删除本地用户和Activiti中的用户
     * @param synToActiviti 是否同步数据到Activiti
     */
	@Override
	public void doDelete(User user, boolean synToActiviti) throws Exception {
		//删除本地用户
		this.baseService.delete(user);
		
		/**
         * 同步删除Activiti User Group
         */
        if (synToActiviti) {
            // 同步删除Activiti User,会自动删除membership对应的信息
            identityService.deleteUser(user.getId().toString());
        }

	}

	@Override
	public void synAllUserAndRoleToActiviti() throws Exception {
		// 清空工作流用户、角色以及关系
        deleteAllActivitiIdentifyData();
        
        // 复制角色数据
        synRoleToActiviti();
        
        // 复制用户以及关系数据
        synUserWithRoleToActiviti();
		
	}

	@Override
	public void deleteAllActivitiIdentifyData() throws Exception {
		this.activitIdentityService.deleteAllMemerShip();
		this.activitIdentityService.deleteAllRole();
		this.activitIdentityService.deleteAllUser();
	}
	
	/**
     * 同步所有角色数据到{@link Group}
	 * @throws Exception 
     */
    private void synRoleToActiviti() throws Exception {
        List<Group> allGroup = this.groupService.getGroupList();
        for (Group group : allGroup) {
            String groupId = group.getId().toString();
            org.activiti.engine.identity.Group identity_group = identityService.newGroup(groupId);
            identity_group.setName(group.getName());
            identity_group.setType(group.getType());
            identityService.saveGroup(identity_group);
        }
    }
    
    /**
     * 复制用户以及关系数据
     * @throws Exception 
     */
    private void synUserWithRoleToActiviti() throws Exception {
        List<User> allUser = this.baseService.getAllList("User",null , null);
        for (User user : allUser) {
            String userId = user.getId().toString();
            String groupId = user.getGroup().getId().toString();
            // 添加一个用户到Activiti
            saveActivitiUser(user);
 
            // 角色和用户的关系
            addMembershipToIdentify(userId, groupId);
        }
    }
    
    /**
     * 更新用户时同事更新membership
     * @param user
     * @throws Exception
     */
    private void updateMembership(User user) throws Exception{
    	this.activitIdentityService.updateMembership(user.getId().toString(), user.getGroup().getId().toString());
    }

}
